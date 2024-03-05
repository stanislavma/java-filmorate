package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZonedDateTime;

/**
 * Film.
 */
@Data
public class Film {
    private int id;

    @NotEmpty
    private String name;

    @Size(max = 200)
    private String description;

    private LocalDate releaseDate;

    @Positive
    private short duration;

}
