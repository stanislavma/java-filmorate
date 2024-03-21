package ru.yandex.practicum.filmorate.model;

import lombok.Getter;

@Getter
public class ErrorResponse {
    // геттеры необходимы, чтобы Spring Boot мог получить значения полей
    String error;

    public ErrorResponse(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "error='" + error + '\'' +
                '}';
    }
}