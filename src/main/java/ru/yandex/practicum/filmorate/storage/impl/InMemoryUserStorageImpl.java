package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class InMemoryUserStorageImpl implements UserStorage {
    private final Map<Integer, User> usersByIdMap = new LinkedHashMap<>();
    private int id;

    @Override
    public User add(User user) {
        user.setId(nextId());
        usersByIdMap.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        usersByIdMap.put(user.getId(), user);
        return user;
    }

    @Override
    public User delete(int id) {
        return null;
    }

    @Override
    public boolean isExist(Integer id) {
        return usersByIdMap.containsKey(id);
    }

    @Override
    public int getCount() {
        return usersByIdMap.size();
    }

    @Override
    public Collection<User> getAll() {
        return usersByIdMap.values();
    }

    private int nextId() {
        return ++id;
    }

}
