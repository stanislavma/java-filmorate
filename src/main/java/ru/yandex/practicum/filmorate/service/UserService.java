package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User add(User user) {
        validateLoginHasSpaces(user.getLogin());
        validateIsLoginAlreadyExist(user.getLogin());
        user = useLoginInsteadNameIfNull(user);

        return userStorage.add(user);
    }

    public User update(User user) {
        validateIsExist(user.getId());
        validateIsLoginAlreadyExist(user.getLogin());

        return userStorage.update(user);
    }

    public boolean isExist(Integer id) {
        return userStorage.isExist(id);
    }

    public int getCount() {
        return userStorage.getCount();
    }

    public Collection<User> getAll() {
        return userStorage.getAll();
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

    private void validateIsExist(Integer id) {
        if (!userStorage.isExist(id)) {
            String errorText = "Id фильма не найден: {}" + id;
            log.error(errorText, id);
            throw new ValidationException(errorText, HttpStatus.NOT_FOUND);
        }
    }

}
