package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class MpaService {
    private final MpaStorage mpaStorage;

    public long getCount() {
        return mpaStorage.getAll().size();
    }

    public Collection<Mpa> getAll() {
        return mpaStorage.getAll();
    }

    public Mpa getById(long id) {
        return mpaStorage.getById(id).orElseThrow(() -> {
            String errorText = "Возрастной рейтинг с таким Id не найден: " + id;
            log.error(errorText);
            return new EntityNotFoundException(errorText);
        });
    }

}
