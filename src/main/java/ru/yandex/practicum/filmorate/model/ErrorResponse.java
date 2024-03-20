package ru.yandex.practicum.filmorate.model;

public class ErrorResponse {
    // название ошибки
    String error;
    // подробное описание
//    String description;

    public ErrorResponse(String error) {
        this.error = error;
//        this.description = description;
    }

    // геттеры необходимы, чтобы Spring Boot мог получить значения полей
    public String getError() {
        return error;
    }

//    public String getDescription() {
//        return description;
//    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "error='" + error + '\'' +
//                ", description='" + description + '\'' +
                '}';
    }
}