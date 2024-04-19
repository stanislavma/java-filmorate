package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {

    User add(User user);

    User update(User user);

    Long delete(long id);

    Optional<User> getById(long id);

    Collection<User> getAll();

}
