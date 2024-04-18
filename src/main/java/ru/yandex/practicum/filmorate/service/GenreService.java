package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreService {
    private final GenreStorage genreStorage;

    public long getCount() {
        return genreStorage.getAll().size();
    }

    public Collection<Genre> getAll() {
        return genreStorage.getAll();
    }

    public Genre getById(long id) {
        return genreStorage.getById(id).orElseThrow(() -> {
            String errorText = "Жанр с таким Id не найден: " + id;
            log.error(errorText);
            return new EntityNotFoundException(errorText);
        });
    }

}
