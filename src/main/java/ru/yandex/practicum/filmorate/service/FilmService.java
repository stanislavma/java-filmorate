package ru.yandex.practicum.filmorate.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;

    private static final LocalDate MIN_FILM_DATE = LocalDateTime.of(1895, 12, 28, 0, 0).toLocalDate();

    public Film add(Film film) {
        validateReleaseDate(film.getReleaseDate());
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        validateIsExist(film.getId());
        return filmStorage.update(film);
    }

    public long getCount() {
        return filmStorage.getAll().size();
    }

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film addLike(long id, long userId) {
        validateIsExist(id);
        userService.validateIsExist(userId);

        Film film = filmStorage.getById(id);

        Set<Long> likes = film.getLikes();
        likes.add(userId);

        filmStorage.update(film);
        return film;
    }

    public Film deleteLike(long id, long userId) {
        validateIsExist(id);
        userService.validateIsExist(userId);

        Film film = filmStorage.getById(id);

        Set<Long> likes = film.getLikes();
        likes.remove(userId);

        filmStorage.update(film);
        return film;
    }

    public Collection<Film> getMostLiked(int count) {
        return filmStorage.getAll().stream()
                .sorted(Comparator.comparingInt((Film o) -> o.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    /**
     * Проверяет наличие фильма с заданным id
     */
    private boolean isExist(long id) {
        return filmStorage.getAll().stream()
                .anyMatch(film -> film.getId().equals(id));
    }

    private void validateIsExist(long id) {
        if (!isExist(id)) {
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
