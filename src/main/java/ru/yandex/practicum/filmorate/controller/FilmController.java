package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exception.InvalidNameException;
import ru.yandex.practicum.filmorate.controller.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("films")
public class FilmController {
    Map<String, Film> filmsByNameMap = new HashMap<>();
    Map<Integer, Film> filmsByIdMap = new HashMap<>();
    private int id;
    private final static LocalDate MIN_FILM_DATE = LocalDateTime.of(1895, 12, 28, 0, 0).toLocalDate();

    @PostMapping()
    public ResponseEntity<?> add(@Valid @RequestBody Film film) {
        log.info(String.valueOf(film));

        try {
            if (film.getReleaseDate().isBefore(MIN_FILM_DATE)) {
                throw new ValidationException("Дата релиза фильма позднее 28 декабря 1895", HttpStatus.BAD_REQUEST);
            }
        } catch (ValidationException e) {
            log.error(e.getMessage());
            return respondError(film, HttpStatus.BAD_REQUEST);
        }

        film.setId(nextId());
        filmsByNameMap.put(film.getName(), film);
        filmsByIdMap.put(film.getId(), film);

        return respondSuccess(film);
    }

    @PutMapping()
    public ResponseEntity<Film> update(@Valid @RequestBody Film film) {
        try {
            if (!filmsByIdMap.containsKey(film.getId())) {
                throw new ValidationException("Id фильма не найден", HttpStatus.NOT_FOUND);
            }
        } catch (ValidationException e) {
            log.error(e.getMessage());
            return respondError(film, HttpStatus.NOT_FOUND);
        }

        String oldFilmNameKey = filmsByIdMap.get(film.getId()).getName();
        filmsByNameMap.remove(oldFilmNameKey);
        filmsByNameMap.put(film.getName(), film);

        filmsByIdMap.put(film.getId(), film);

        return respondSuccess(film);
    }

    @GetMapping()
    public ResponseEntity<Collection<Film>> getAll() {
        log.info("Текущее количество фильмов: " + filmsByNameMap.size());

        return respondListSuccess(filmsByNameMap.values());
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
