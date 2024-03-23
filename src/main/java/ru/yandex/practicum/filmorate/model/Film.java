package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
@Jacksonized
@Builder
public class Film {
    private Long id;

    @NotEmpty
    private String name;

    @Size(max = 200, message = "description must be less then 200 symbols")
    private String description;

    private LocalDate releaseDate;

    @Positive(message = "Must be positive number")
    private Short duration;

    @Builder.Default
    private Set<Long> likes = new LinkedHashSet<>();
}
