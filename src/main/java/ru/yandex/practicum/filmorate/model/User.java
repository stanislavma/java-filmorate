package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

/**
 * User
 */
@Data
@Builder(toBuilder = true)
public class User {
    int id;

    @Email
    @NotNull
    String email;

    @NotNull
    @NotEmpty
    String login;

    @Builder.Default
    String name = "";

    @PastOrPresent
    LocalDate birthday;
}
