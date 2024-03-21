package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private static final LocalDate MIN_FILM_DATE = LocalDateTime.of(1895, 12, 28, 0, 0).toLocalDate();

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film add(Film film) {
        validateReleaseDate(film.getReleaseDate());
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        validateIsExist(film.getId());
        return filmStorage.update(film);
    }

    public int getCount() {
        return filmStorage.getCount();
    }

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    private void validateIsExist(Integer id) {
        if (!filmStorage.isExist(id)) {
            log.error("Id фильма не найден: {}", id);
            throw new ValidationException("Id фильма не найден: " + id, HttpStatus.NOT_FOUND);
        }
    }

    private void validateReleaseDate(LocalDate releaseDate) {
        if (releaseDate.isBefore(MIN_FILM_DATE)) {
            log.error("Дата релиза фильма позднее 28 декабря 1895");
            throw new ValidationException("Дата релиза фильма позднее 28 декабря 1895", HttpStatus.BAD_REQUEST);
        }
    }

}
