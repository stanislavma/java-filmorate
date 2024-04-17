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
        validateIsExist(id);
        return mpaStorage.getById(id).orElse(null);
    }

    /**
     * Проверяет наличие возрастного рейтинга с заданным id
     */
    private boolean isExist(long id) {
        return mpaStorage.getById(id).isPresent();
    }

    protected void validateIsExist(long id) {
        if (!isExist(id)) {
            String errorText = "Возрастной рейтинг с таким Id не найден: " + id;
            log.error(errorText);
            throw new EntityNotFoundException(errorText);
        }
    }

}
