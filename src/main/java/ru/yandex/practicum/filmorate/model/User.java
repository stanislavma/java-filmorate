package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * User
 */
@Data
@Jacksonized
@Builder(toBuilder = true)
public class User {
    private Long id;

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

    @Builder.Default
    private Set<Long> friends = new LinkedHashSet<>();

}
