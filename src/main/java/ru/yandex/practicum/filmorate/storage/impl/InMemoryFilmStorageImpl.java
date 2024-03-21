package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Component
public class InMemoryFilmStorageImpl implements FilmStorage {
    private final Map<Long, Film> filmsByIdMap = new LinkedHashMap<>();
    private long id;

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
    public Film delete(long id) {
        return null;
    }

    @Override
    public Film addLike(long id, long userId) {
        Film film = filmsByIdMap.get(id);

        Set<Long> likes = film.getLikes();
        likes.add(userId);

        filmsByIdMap.put(film.getId(), film);
        return film;
    }
    @Override
    public Film deleteLike(long id, long userId) {
        Film film = filmsByIdMap.get(id);

        Set<Long> likes = film.getLikes();
        likes.remove(userId);

        filmsByIdMap.put(film.getId(), film);
        return film;
    }

    @Override
    public boolean isExist(long id) {
        return filmsByIdMap.containsKey(id);
    }

    @Override
    public long getCount() {
        return filmsByIdMap.size();
    }

    @Override
    public Collection<Film> getAll() {
        return filmsByIdMap.values();
    }


    private long nextId() {
        return ++id;
    }


}
