package ru.yandex.practicum.filmorate.controller.exception;

public class InvalidNameException extends RuntimeException {

    public InvalidNameException(String message) {
        super(message);
    }
}
