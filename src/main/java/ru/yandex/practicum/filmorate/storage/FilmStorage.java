package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Film add(Film film);

    Film update(Film film);

    Film delete(int id);

    boolean isExist(Integer id);

    int getCount();

    Collection<Film> getAll();
}
