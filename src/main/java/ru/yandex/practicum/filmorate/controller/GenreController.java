package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

import static ru.yandex.practicum.filmorate.util.ResponseUtil.respondSuccess;
import static ru.yandex.practicum.filmorate.util.ResponseUtil.respondSuccessList;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("genres")
public class GenreController {
    private final GenreService genreService;

    @GetMapping()
    public ResponseEntity<Collection<Genre>> getAll() {
        log.info("Получить все жанры");
        log.info("Текущее количество жанров: {}", genreService.getCount());

        return respondSuccessList(genreService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Genre> getById(@PathVariable long id) {
        log.info("Получить жанр по {}", id);

        return respondSuccess(genreService.getById(id));
    }

}
