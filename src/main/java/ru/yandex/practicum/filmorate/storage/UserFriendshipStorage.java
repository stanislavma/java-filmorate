package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserFriendshipStorage {

    Long addUserToFriend(Long userIdOne, Long userIdTwo);

    Long deleteFriend(Long userIdOne, Long userIdTwo);

    Collection<User> getFriends(Long userId);

}
