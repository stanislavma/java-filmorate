package ru.yandex.practicum.filmorate.storage.impl.memory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryUserStorageImpl implements UserStorage {
    private long id;
    private final Map<Long, User> usersByIdMap = new LinkedHashMap<>();

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
    public Long delete(long id) {
        return null;
    }

    @Override
    public Collection<User> getAll() {
        return usersByIdMap.values();
    }

    @Override
    public Optional<User> getById(long id) {
        return Optional.ofNullable(usersByIdMap.get(id));
    }

    private long nextId() {
        return ++id;
    }

}
