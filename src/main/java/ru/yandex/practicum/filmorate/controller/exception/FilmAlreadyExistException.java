package ru.yandex.practicum.filmorate.controller.exception;

public class FilmAlreadyExistException extends RuntimeException {

    public FilmAlreadyExistException(String message) {
        super(message);
    }

}
