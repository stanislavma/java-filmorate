package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;

import static ru.yandex.practicum.filmorate.util.ResponseUtil.respondSuccess;
import static ru.yandex.practicum.filmorate.util.ResponseUtil.respondSuccessList;

@Slf4j
@RestController
@RequestMapping("films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping()
    public ResponseEntity<Film> add(@Valid @RequestBody Film film) {
        log.info("Добавление фильма");

        Film addedFilm = filmService.add(film);
        log.info("Фильм успешно добавлен");

        return respondSuccess(addedFilm);
    }

    @PutMapping()
    public ResponseEntity<Film> update(@Valid @RequestBody Film film) {
        log.info("Обновление фильма");

        Film updatedFilm = filmService.update(film);

        log.info("Фильм успешно обновлен");
        return respondSuccess(updatedFilm);
    }

    @GetMapping()
    public ResponseEntity<Collection<Film>> getAll() {
        log.info("Текущее количество фильмов: " + filmService.getCount());

        return respondSuccessList(filmService.getAll());
    }

}
