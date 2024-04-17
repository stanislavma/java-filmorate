package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {

    Film add(Film film);

    Film update(Film film);

    Long delete(long id);

    Optional<Film> getById(long id);

    Collection<Film> getAll();

}
