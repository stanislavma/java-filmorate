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

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<User> addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Пользователь {} хочет добавить в друзья пользователя {}", id, friendId);

        User updatedUser = userService.addFriend(id, friendId);
        log.info("Друг успешно добавлен");

        return respondSuccess(updatedUser);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<User> deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Пользователь {} хочет удалить из друзей пользователя {}", id, friendId);

        User updatedUser = userService.deleteFriend(id, friendId);
        log.info("Друг успешно удален");

        return respondSuccess(updatedUser);
    }

    @GetMapping("/{id}/friends/common/{friendId}")
    public ResponseEntity<Collection<User>> getCommonFriends(@PathVariable long id, @PathVariable long friendId) {
        log.info("Общие друзья пользователей {} и {}", id, friendId);

        Collection<User> commonUsers = userService.getCommonFriends(id, friendId);
        log.info("Общие друзья успешно получены");

        return respondSuccessList(commonUsers);
    }

}
