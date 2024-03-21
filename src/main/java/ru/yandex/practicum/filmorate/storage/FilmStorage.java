package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Film add(Film film);

    Film update(Film film);

    Film delete(long id);

    boolean isExist(long id);

    long getCount();

    Collection<Film> getAll();

    Film addLike(long id, long userId);

    Film deleteLike(long id, long userId);

}
