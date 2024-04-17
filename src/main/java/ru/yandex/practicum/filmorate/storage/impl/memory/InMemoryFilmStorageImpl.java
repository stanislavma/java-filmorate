package ru.yandex.practicum.filmorate.storage.impl.memory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryFilmStorageImpl implements FilmStorage {
    private long id;
    private final Map<Long, Film> filmsByIdMap = new LinkedHashMap<>();

    @Override
    public Film add(Film film) {
        film.setId(nextId());
        filmsByIdMap.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        filmsByIdMap.put(film.getId(), film);
        return film;
    }

    @Override
    public Long delete(long id) {
        return null;
    }

    @Override
    public Collection<Film> getAll() {
        return filmsByIdMap.values();
    }

    @Override
    public Optional<Film> getById(long id) {
        return Optional.ofNullable(filmsByIdMap.get(id));
    }

    private long nextId() {
        return ++id;
    }

}
