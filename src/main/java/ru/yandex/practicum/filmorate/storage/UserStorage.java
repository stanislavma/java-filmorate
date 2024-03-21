package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    User add(User user);

    User update(User user);

    User delete(long id);

    boolean isExist(long id);

    long getCount();

    Collection<User> getAll();

    User getById(long id);

    User addFriend(long id, long friendId);

    User deleteFriend(long id, long friendId);

}
