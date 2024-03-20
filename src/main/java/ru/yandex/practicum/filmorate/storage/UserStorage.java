package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    User add(User user);

    User update(User user);

    User delete(int id);

    boolean isExist(Integer id);

    int getCount();

    Collection<User> getAll();
}
