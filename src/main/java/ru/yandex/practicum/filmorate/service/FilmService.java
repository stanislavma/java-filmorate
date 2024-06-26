package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmUserLikeStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

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
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;
    private final FilmUserLikeStorage filmUserLikeStorage;

    private static final LocalDate MIN_FILM_DATE = LocalDateTime.of(1895, 12, 28, 0, 0).toLocalDate();

    public FilmService(@Qualifier("dbFilmStorageImpl") FilmStorage filmStorage, UserService userService, MpaStorage mpaStorage, GenreStorage genreStorage, FilmUserLikeStorage filmUserLikeStorage) {
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
        this.filmUserLikeStorage = filmUserLikeStorage;
    }

    public Film add(Film film) {
        validateReleaseDate(film.getReleaseDate());

        if (film.getMpa() != null && film.getMpa().getId() != null) {
            validateIsMpaExist(film.getMpa().getId());
        }

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                validateIsGenreExist(genre.getId());
            }
        }

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

    public Film getById(long id) {
        return filmStorage.getById(id).orElseThrow(() -> {
            String errorText = "Фильм с таким Id не найден: " + id;
            log.error(errorText);
            return new EntityNotFoundException(errorText);
        });
    }

    public Collection<Long> getLikes(long id) {
        return filmUserLikeStorage.getLikes(id);
    }

    public Film addLike(long id, long userId) {
        Film film = getById(id); // если фильма нету то метод сам пробросит ошибку
        userService.getById(userId); // если пользователя нету то метод сам пробросит ошибку

        filmUserLikeStorage.addLike(id, userId); //добавляем лайк от существующего пользователя существующему фильму
        return film; //вот этот вопрос надо ли возвращать фильм при добавлении лайков, если в нем не содержится лайков, но это уже не важно
    }

    public Film deleteLike(long id, long userId) {
        validateIsExist(id);
        userService.validateIsExist(userId);

        filmUserLikeStorage.deleteLike(id, userId);
        return filmStorage.getById(id).orElse(null);
    }

    public Collection<Film> getMostLiked(int count) {
        return filmStorage.getAll().stream().sorted(Comparator.comparingInt((Film o) -> o.getLikes().size()).reversed()).limit(count).collect(Collectors.toList());
    }

    private void validateIsExist(long id) {
        if (!isExist(id)) {
            String errorText = "Фильм с таким Id не найден: " + id;
            throw new EntityNotFoundException(errorText);
        }
    }

    /**
     * Проверяет наличие фильма с заданным id
     */
    private boolean isExist(long id) {
        return filmStorage.getById(id).isPresent();
    }

    private void validateIsMpaExist(long id) {
        if (!isMpaExist(id)) {
            String errorText = "Возрастное ограничение с таким Id не найдено: " + id;
            throw new ValidationException(errorText, HttpStatus.BAD_REQUEST);
        }
    }

    private boolean isMpaExist(long id) {
        return mpaStorage.getById(id).isPresent();
    }

    private void validateIsGenreExist(long id) {
        if (!isGenreExist(id)) {
            String errorText = "Жанр с таким Id не найден: " + id;
            throw new ValidationException(errorText, HttpStatus.BAD_REQUEST);
        }
    }

    private boolean isGenreExist(long id) {
        return genreStorage.getById(id).isPresent();
    }

    private void validateReleaseDate(LocalDate releaseDate) {
        if (releaseDate.isBefore(MIN_FILM_DATE)) {
            log.error("Дата релиза фильма позднее 28 декабря 1895");
            throw new ValidationException("Дата релиза фильма позднее 28 декабря 1895", HttpStatus.BAD_REQUEST);
        }
    }

}
