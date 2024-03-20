package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

import static ru.yandex.practicum.filmorate.util.ResponseUtil.respondError;
import static ru.yandex.practicum.filmorate.util.ResponseUtil.respondSuccess;

@Slf4j
@RestController
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<User> add(@Valid @RequestBody User user) {
        log.info("Добавление пользователя");

        if (user.getLogin().contains("\\S+")) {
            log.error("Логин не может содержать пробелы");
            return respondError(user, HttpStatus.BAD_REQUEST);
        }

        final String newUserLogin = user.getLogin();
        boolean isLoginAlreadyExist = userService.getAll().stream()
                .anyMatch(oldUser -> oldUser.getLogin().equalsIgnoreCase(newUserLogin));

        if (isLoginAlreadyExist) {
            log.error("Пользователь с таким login уже существует");
            return respondError(user, HttpStatus.BAD_REQUEST);
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            user = user.toBuilder().name(user.getLogin()).build();
        }

        User addedUser = userService.add(user);
        log.info("Пользователь успешно добавлен");

        return respondSuccess(addedUser);
    }

    @PutMapping()
    public ResponseEntity<User> update(@Valid @RequestBody User user) {
        log.info("Обновление пользователя");

        if (!userService.isExist(user.getId())) {
            log.error("Id пользователя не найден");
            return respondError(user, HttpStatus.NOT_FOUND);
        }

        final String newUserLogin = user.getLogin();
        boolean isLoginAlreadyExist = userService.getAll().stream()
                .anyMatch(oldUser -> oldUser.getLogin().equalsIgnoreCase(newUserLogin));

        if (isLoginAlreadyExist) {
            log.error("Пользователь с таким login уже существует");
            return respondError(user, HttpStatus.BAD_REQUEST);
        }

        User updatedUser = userService.update(user);
        log.info("Пользователь успешно обновлен");

        return respondSuccess(updatedUser);
    }

    @GetMapping()
    public ResponseEntity<Collection<User>> getAll() {
        log.info("Текущее количество пользователей: " + userService.getCount());

        return respondSuccess(userService.getAll());
    }

}
