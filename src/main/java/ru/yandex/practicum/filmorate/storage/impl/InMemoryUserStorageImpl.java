package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Component
public class InMemoryUserStorageImpl implements UserStorage {
    private final Map<Long, User> usersByIdMap = new LinkedHashMap<>();
    //    private final Map<Long, Set<Long>> userFriendsIdMap = new LinkedHashMap<>();
    private long id;

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
    public User delete(long id) {
        return null;
    }

    @Override
    public User addFriend(long id, long friendId) {
        User user = addToFriend(id, friendId);
        addToFriend(friendId, id); // Добавить друга взаимно

        return user;
    }

    private User addToFriend(long id, long friendId) {
        User user = usersByIdMap.get(id);

        Set<Long> userFriends = user.getFriends();
        userFriends.add(friendId);

        usersByIdMap.put(user.getId(), user);
        return user;
    }

    @Override
    public boolean isExist(long id) {
        return usersByIdMap.containsKey(id);
    }

    @Override
    public long getCount() {
        return usersByIdMap.size();
    }

    @Override
    public Collection<User> getAll() {
        return usersByIdMap.values();
    }

    private long nextId() {
        return ++id;
    }

}
