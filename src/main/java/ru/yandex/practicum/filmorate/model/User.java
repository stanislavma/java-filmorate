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
    private Integer id;

    @Email
    @NotNull
    private String email;

    @NotNull
    @NotEmpty
    private String login;

    @Builder.Default
    private String name = "";

    @PastOrPresent
    private LocalDate birthday;
}
