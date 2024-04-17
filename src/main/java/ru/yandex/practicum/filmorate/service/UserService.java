package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserFriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private final UserFriendshipStorage userFriendshipStorage;

    public UserService(@Qualifier("dbUserStorageImpl") UserStorage userStorage,
                       UserFriendshipStorage userFriendshipStorage) {
        this.userStorage = userStorage;
        this.userFriendshipStorage = userFriendshipStorage;
    }

    public User add(User user) {
        validateLoginHasSpaces(user.getLogin());
        validateIsLoginAlreadyExist(user.getLogin());
        user = useLoginInsteadNameIfNull(user);

        return userStorage.add(user);
    }

    public User update(User user) {
        if (user.getId() == null) {
            return user;
        }

        validateIsExist(user.getId());
        validateIsLoginAlreadyExist(user.getLogin());

        return userStorage.update(user);
    }

    public long getCount() {
        return userStorage.getAll().size();
    }

    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    public User getById(long id) {
        validateIsExist(id);
        return userStorage.getById(id);
    }

    public User addFriend(long id, long friendId) {
        validateIsExist(id);
        validateIsExist(friendId);
        validateSameUser(id, friendId);

//        User user = addUserToFriend(id, friendId);

        userFriendshipStorage.addUserToFriend(id, friendId); // Добавить друга

        return userStorage.getById(id);
    }

    public User deleteFriend(long id, long friendId) {
        validateIsExist(id);
        validateIsExist(friendId);

        userFriendshipStorage.deleteFriend(id, friendId); // Удалить друга

        return userStorage.getById(id);
    }

    public Collection<User> getUserFriends(long id) {
        validateIsExist(id);

        return userFriendshipStorage.getFriends(id);
    }

    public Collection<User> getCommonFriends(long id, long friendId) {
        validateIsExist(id);
        validateIsExist(friendId);
        validateSameUser(id, friendId);

        Collection<User> userOneFriendships = userFriendshipStorage.getFriends(id);
        Collection<User> userTwoFriendships = userFriendshipStorage.getFriends(friendId);

        Set<Long> firstUserFriends = userOneFriendships.stream()
                .map(User::getId)
                .collect(Collectors.toSet());

        Set<Long> secondUserFriends = userTwoFriendships.stream()
                .map(User::getId)
                .collect(Collectors.toSet());

        return firstUserFriends.stream()
                .filter(secondUserFriends::contains)
                .map(userStorage::getById)
                .collect(Collectors.toSet());
    }

    /**
     * Проверяет наличие пользователя с заданным id
     */
    private boolean isExist(long id) {
        return userStorage.getAll().stream()
                .anyMatch(user -> user.getId().equals(id));
    }

    /**
     * Если пользователь без имени, подставить в имя - логин
     */
    private User useLoginInsteadNameIfNull(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            return user.toBuilder().name(user.getLogin()).build();
        }
        return user;
    }

    private void validateLoginHasSpaces(String login) {
        if (login.contains("\\S+")) {
            String errorText = "Логин не может содержать пробелы";
            log.error(errorText);
            throw new ValidationException(errorText, HttpStatus.BAD_REQUEST);
        }
    }

    private void validateIsLoginAlreadyExist(String newUserLogin) {
        boolean isLoginAlreadyExist = userStorage.getAll().stream()
                .anyMatch(oldUser -> oldUser.getLogin().equalsIgnoreCase(newUserLogin));

        if (isLoginAlreadyExist) {
            String errorText = "Пользователь с таким login уже существует";
            log.error(errorText);
            throw new ValidationException(errorText, HttpStatus.BAD_REQUEST);
        }
    }

    protected void validateIsExist(long id) {
        if (!isExist(id)) {
            String errorText = "Пользователь с таким Id не найден: " + id;
            log.error(errorText);
            throw new EntityNotFoundException(errorText);
        }
    }

    private void validateSameUser(long id, long friendId) {
        if (id == friendId) {
            String errorText = "Невозможно обработать запрос с одинаковыми id пользователей: " + id + ", " + friendId;
            log.error(errorText);
            throw new ValidationException(errorText, HttpStatus.BAD_REQUEST);
        }
    }

}
