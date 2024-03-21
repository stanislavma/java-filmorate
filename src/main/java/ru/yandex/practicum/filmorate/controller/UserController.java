package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

import static ru.yandex.practicum.filmorate.util.ResponseUtil.respondSuccess;
import static ru.yandex.practicum.filmorate.util.ResponseUtil.respondSuccessList;

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

        User addedUser = userService.add(user);
        log.info("Пользователь успешно добавлен");

        return respondSuccess(addedUser);
    }

    @PutMapping()
    public ResponseEntity<User> update(@Valid @RequestBody User user) {
        log.info("Обновление пользователя");

        User updatedUser = userService.update(user);
        log.info("Пользователь успешно обновлен");

        return respondSuccess(updatedUser);
    }

    @GetMapping()
    public ResponseEntity<Collection<User>> getAll() {
        log.info("Получить всех пользователей");
        log.info("Текущее количество пользователей: " + userService.getCount());

        return respondSuccessList(userService.getAll());
    }

}
