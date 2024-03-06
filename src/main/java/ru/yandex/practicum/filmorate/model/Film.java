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

    @Size(max = 200)
    private String description;

    private LocalDate releaseDate;

    @Positive
    private short duration;

}
