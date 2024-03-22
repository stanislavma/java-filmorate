package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
@RequestMapping("films")
public class FilmController {
    private final FilmService filmService;

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
        log.info("Получить все фильмы");
        log.info("Текущее количество фильмов: " + filmService.getCount());

        return respondSuccessList(filmService.getAll());
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<Film> addLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Пользователь {} ставит like фильму {}", userId, id);

        Film updatedFilm = filmService.addLike(id, userId);
        log.info("Like успешно поставлен");

        return respondSuccess(updatedFilm);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<Film> deleteLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Пользователь {} удаляет like фильму {}", userId, id);

        Film updatedFilm = filmService.deleteLike(id, userId);
        log.info("Like успешно удален");

        return respondSuccess(updatedFilm);
    }

    @GetMapping("/popular")
    public ResponseEntity<Collection<Film>> getMostLiked(@RequestParam(defaultValue = "10", required = false) Integer count) {
        log.info("Получить список фильмов с наибольшим количеством лайков");

        Collection<Film> films = filmService.getMostLiked(count);
        log.info("Фильмы с наибольшим количеством лайков успешно получены");

        return respondSuccess(films);
    }

}
