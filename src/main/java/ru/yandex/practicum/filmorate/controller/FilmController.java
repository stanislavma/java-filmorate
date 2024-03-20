package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

import static ru.yandex.practicum.filmorate.util.ResponseUtil.respondError;
import static ru.yandex.practicum.filmorate.util.ResponseUtil.respondSuccess;

@Slf4j
@RestController
@RequestMapping("films")
public class FilmController {
    private final FilmService filmService;
    private static final LocalDate MIN_FILM_DATE = LocalDateTime.of(1895, 12, 28, 0, 0).toLocalDate();

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping()
    public ResponseEntity<Film> add(@Valid @RequestBody Film film) {
        log.info("Добавление фильма");

        if (film.getReleaseDate().isBefore(MIN_FILM_DATE)) {
            log.error("Дата релиза фильма позднее 28 декабря 1895");
            return respondError(film, HttpStatus.BAD_REQUEST);
        }

        Film addedFilm = filmService.add(film);
        log.info("Фильм успешно добавлен");

        return respondSuccess(addedFilm);
    }

    @PutMapping()
    public ResponseEntity<Film> update(@Valid @RequestBody Film film) {
        log.info("Обновление фильма");

        if (!filmService.isExist(film.getId())) {
            log.error("Id фильма не найден");
            return respondError(film, HttpStatus.NOT_FOUND);
        }

        Film updatedFilm = filmService.update(film);

        log.info("Фильм успешно обновлен");
        return respondSuccess(updatedFilm);
    }

    @GetMapping()
    public ResponseEntity<Collection<Film>> getAll() {
        log.info("Текущее количество фильмов: " + filmService.getCount());

        return respondSuccess(filmService.getAll());
    }

}
