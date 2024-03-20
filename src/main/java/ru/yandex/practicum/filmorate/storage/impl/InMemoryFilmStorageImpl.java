package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorageImpl implements FilmStorage {
    private final Map<Integer, Film> filmsByIdMap = new LinkedHashMap<>();
    private int id;

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
    public Film delete(int id) {
        return null;
    }

    @Override
    public boolean isExist(Integer id) {
        return filmsByIdMap.containsKey(id);
    }

    @Override
    public int getCount() {
        return filmsByIdMap.size();
    }

    @Override
    public Collection<Film> getAll() {
        return filmsByIdMap.values();
    }

    private int nextId() {
        return ++id;
    }


}
