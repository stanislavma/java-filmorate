package ru.yandex.practicum.filmorate.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    public static <T> ResponseEntity<T> respondSuccess(T body) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(body);
    }

    public static <T> ResponseEntity<T> respondError(T body, HttpStatus httpStatus) {
        return ResponseEntity
                .status(httpStatus)
                .body(body);
    }

}