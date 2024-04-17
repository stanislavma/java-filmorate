package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Film.
 */
@Data
@Jacksonized
@Builder(toBuilder = true)
public class Film {
    private Long id;

    @NotEmpty
    @Size(max = 75, message = "name must be less then 75 symbols")
    private String name;

    @Size(max = 200, message = "description must be less then 200 symbols")
    private String description;

    private LocalDate releaseDate;

    @Positive(message = "Must be positive number")
    private Short duration;

    private Mpa mpa; // возрастной рейтинг MPA

    private Set<Genre> genres; // список жанров

    @Builder.Default
    private Set<Long> likes = new LinkedHashSet<>();

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("description", description);
        values.put("release_date", releaseDate);
        values.put("duration", duration);

        if (mpa != null && mpa.getId() != null) {
            values.put("mpa", mpa.getId());
        }

        values.put("genres", genres);
        values.put("likes", likes);
        return values;
    }

}
