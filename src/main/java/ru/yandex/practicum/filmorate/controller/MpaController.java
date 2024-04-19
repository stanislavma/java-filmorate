package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

import static ru.yandex.practicum.filmorate.util.ResponseUtil.respondSuccess;
import static ru.yandex.practicum.filmorate.util.ResponseUtil.respondSuccessList;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService mpaService;

    @GetMapping()
    public ResponseEntity<Collection<Mpa>> getAll() {
        log.info("Получить все возрастные рейтинги");
        log.info("Текущее количество возрастных рейтингов: {}", mpaService.getCount());

        return respondSuccessList(mpaService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mpa> getById(@PathVariable long id) {
        log.info("Получить возрастной рейтинг по {}", id);

        return respondSuccess(mpaService.getById(id));
    }

}
