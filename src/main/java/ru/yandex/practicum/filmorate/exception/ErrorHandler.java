package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@RestControllerAdvice("ru.yandex.practicum.filmorate")
public class ErrorHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(errorResponse, e.getStatus());
    }

//    @ExceptionHandler(EntityNotFoundException.class)
//    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
//        ErrorResponse errorResponse = new ErrorResponse("Entity Not Found", e.getMessage());
//        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
//    }

//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ErrorResponse handle1(final PostNotFoundException e) {
//        return new ErrorResponse(
//                "error", e.getMessage()
//        );
//    }
//
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ErrorResponse handle2(final UserNotFoundException e) {
//        return new ErrorResponse(
//                "error", e.getMessage()
//        );
//    }
//
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.CONFLICT)
//    public ErrorResponse handle3(final UserAlreadyExistException e) {
//        return new ErrorResponse(
//                "error", e.getMessage()
//        );
//    }
//
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ErrorResponse handle4(final InvalidEmailException e) {
//        return new ErrorResponse(
//                "error", e.getMessage()
//        );
//    }
//
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ErrorResponse handle5(final IncorrectParameterException e) {
//        return new ErrorResponse(
//                "error", "Ошибка с полем " + e.getParameter()
//        );
//    }
//
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ErrorResponse handle6(final Throwable e) {
//        return new ErrorResponse(
//                "error", "Произошла непредвиденная ошибка."
//        );
//    }

} 