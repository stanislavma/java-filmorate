package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("users")
public class UserController {
    private final Map<Integer, User> usersByIdMap = new LinkedHashMap<>();
    private int id;

    @PostMapping()
    public ResponseEntity<User> add(@Valid @RequestBody User user) {
        log.info("Добавление пользователя");

        if (user.getLogin().contains("\\S+")) {
            log.error("Логин не может содержать пробелы");
            return respondError(user, HttpStatus.BAD_REQUEST);
        }

        final String newUserLogin = user.getLogin();
        boolean isLoginAlreadyExist = usersByIdMap.values().stream()
                .anyMatch(oldUser -> oldUser.getLogin().equalsIgnoreCase(newUserLogin));

        if (isLoginAlreadyExist) {
            log.error("Пользователь с таким login уже существует");
            return respondError(user, HttpStatus.BAD_REQUEST);
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            user = user.toBuilder().name(user.getLogin()).build();
        }

        user.setId(nextId());
        usersByIdMap.put(user.getId(), user);

        log.info("Пользователь успешно добавлен");

        return respondSuccess(user);
    }

    @PutMapping()
    public ResponseEntity<User> update(@Valid @RequestBody User user) {
        log.info("Обновление пользователя");

        if (!usersByIdMap.containsKey(user.getId())) {
            log.error("Id пользователя не найден");
            return respondError(user, HttpStatus.NOT_FOUND);
        }

        final String newUserLogin = user.getLogin();
        boolean isLoginAlreadyExist = usersByIdMap.values().stream()
                .anyMatch(oldUser -> oldUser.getLogin().equalsIgnoreCase(newUserLogin));

        if (isLoginAlreadyExist) {
            log.error("Пользователь с таким login уже существует");
            return respondError(user, HttpStatus.BAD_REQUEST);
        }

        usersByIdMap.put(user.getId(), user);

        log.info("Пользователь успешно обновлен");

        return respondSuccess(user);
    }

    @GetMapping()
    public ResponseEntity<Collection<User>> getAll() {
        log.info("Текущее количество пользователей: " + usersByIdMap.size());

        return respondListSuccess(usersByIdMap.values());
    }

    private int nextId() {
        return ++id;
    }

    private static ResponseEntity<User> respondSuccess(User user) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(user);
    }

    private static ResponseEntity<Collection<User>> respondListSuccess(Collection<User> users) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(users);
    }

    private static ResponseEntity<User> respondError(User user, HttpStatus httpStatus) {
        return ResponseEntity
                .status(httpStatus)
                .body(user);
    }
}
