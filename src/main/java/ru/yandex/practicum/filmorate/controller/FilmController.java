package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("films")
public class FilmController {
    private final Map<Integer, Film> filmsByIdMap = new LinkedHashMap<>();
    private int id;
    private static final LocalDate MIN_FILM_DATE = LocalDateTime.of(1895, 12, 28, 0, 0).toLocalDate();

    @PostMapping()
    public ResponseEntity<Film> add(@Valid @RequestBody Film film) {
        log.info("Добавление фильма");

        if (film.getReleaseDate().isBefore(MIN_FILM_DATE)) {
            log.error("Дата релиза фильма позднее 28 декабря 1895");
            return respondError(film, HttpStatus.BAD_REQUEST);
        }

        film.setId(nextId());
        filmsByIdMap.put(film.getId(), film);

        log.info("Фильм успешно добавлен");

        return respondSuccess(film);
    }

    @PutMapping()
    public ResponseEntity<Film> update(@Valid @RequestBody Film film) {
        log.info("Обновление фильма");

        if (!filmsByIdMap.containsKey(film.getId())) {
            log.error("Id фильма не найден");
            return respondError(film, HttpStatus.NOT_FOUND);
        }

        filmsByIdMap.put(film.getId(), film);

        log.info("Фильм успешно обновлен");

        return respondSuccess(film);
    }

    @GetMapping()
    public ResponseEntity<Collection<Film>> getAll() {
        log.info("Текущее количество фильмов: " + filmsByIdMap.size());

        return respondListSuccess(filmsByIdMap.values());
    }

    private int nextId() {
        return ++id;
    }

    private static ResponseEntity<Film> respondSuccess(Film film) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(film);
    }

    private static ResponseEntity<Collection<Film>> respondListSuccess(Collection<Film> films) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(films);
    }

    private static ResponseEntity<Film> respondError(Film film, HttpStatus httpStatus) {
        return ResponseEntity
                .status(httpStatus)
                .body(film);
    }

}
