package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;
    private static final LocalDate MIN_FILM_DATE = LocalDateTime.of(1895, 12, 28, 0, 0).toLocalDate();

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Film add(Film film) {
        validateReleaseDate(film.getReleaseDate());
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        validateIsExist(film.getId());
        return filmStorage.update(film);
    }

    public long getCount() {
        return filmStorage.getCount();
    }

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film addLike(long id, long userid) {
        validateIsExist(id);
        userService.validateIsExist(userid);

        return filmStorage.addLike(id, userid);
    }

    public Film deleteLike(long id, long userid) {
        validateIsExist(id);
        userService.validateIsExist(userid);

        return filmStorage.deleteLike(id, userid);
    }

    public Collection<Film> getMostLiked(int count) {
        return filmStorage.getAll().stream()
                .sorted(Comparator.comparingInt((Film o) -> o.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    private void validateIsExist(long id) {
        if (!filmStorage.isExist(id)) {
            String errorText = "Фильм с таким Id не найден: " + id;
            throw new EntityNotFoundException(errorText);
        }
    }

    private void validateReleaseDate(LocalDate releaseDate) {
        if (releaseDate.isBefore(MIN_FILM_DATE)) {
            log.error("Дата релиза фильма позднее 28 декабря 1895");
            throw new ValidationException("Дата релиза фильма позднее 28 декабря 1895", HttpStatus.BAD_REQUEST);
        }
    }

}
