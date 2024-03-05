package ru.yandex.practicum.filmorate.controller.exception;

public class UserAlreadyExistException extends RuntimeException {

    public UserAlreadyExistException(String message) {
        super(message);
    }

}
