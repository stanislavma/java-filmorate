package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("users")
public class UserController {
    Map<String, User> usersByLoginMap = new HashMap<>();
    Map<Integer, User> usersByIdMap = new HashMap<>();
    private int id;

    @PostMapping()
    public ResponseEntity<?> add(@Valid @RequestBody User user) {
        log.info("Добавление пользователя");

        try {
            if (user.getLogin().contains("\\S+")) {
                throw new ValidationException("Логин не может содержать пробелы", HttpStatus.BAD_REQUEST);
            }
        } catch (ValidationException e) {
            log.error(e.getMessage());
            return respondError(user, HttpStatus.BAD_REQUEST);
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            user = user.toBuilder().name(user.getLogin()).build();
        }

        user.setId(nextId());
        usersByLoginMap.put(user.getLogin(), user);
        usersByIdMap.put(user.getId(), user);

        log.info("Пользователь успешно добавлен");

        return respondSuccess(user);
    }

    @PutMapping()
    public ResponseEntity<User> update(@Valid @RequestBody User user) {
        log.info("Обновление пользователя");

        try {
            if (!usersByIdMap.containsKey(user.getId())) {
                throw new ValidationException("Id фильма не найден", HttpStatus.NOT_FOUND);
            }
        } catch (ValidationException e) {
            log.error(e.getMessage());
            return respondError(user, HttpStatus.NOT_FOUND);
        }

        String oldUserLoginKey = usersByIdMap.get(user.getId()).getLogin();
        usersByLoginMap.remove(oldUserLoginKey);
        usersByLoginMap.put(user.getName(), user);

        usersByIdMap.put(user.getId(), user);

        log.info("Пользователь успешно обновлен");

        return respondSuccess(user);
    }

    @GetMapping()
    public ResponseEntity<Collection<User>> getAll() {
        log.info("Текущее количество пользователей: " + usersByLoginMap.size());

        return respondListSuccess(usersByLoginMap.values());
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
