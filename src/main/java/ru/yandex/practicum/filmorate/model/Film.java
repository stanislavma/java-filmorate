package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
/**
 * Film.
 */
@Data
@Builder
public class Film {
    private int id;

    @NotEmpty
    private String name;

    @Size(max = 200, message = "description must be less then 200 symbols")
    private String description;

    private LocalDate releaseDate;

    @Positive(message = "Must be positive number")
    private short duration;

}
