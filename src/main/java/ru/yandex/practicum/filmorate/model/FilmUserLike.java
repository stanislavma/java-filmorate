package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.util.HashMap;
import java.util.Map;

/**
 * Film.
 */
@Data
@Jacksonized
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class FilmUserLike {
    private Long id;
    private final Long filmId;
    private final Long userId;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("filmId", filmId);
        values.put("userId", userId);

        return values;
    }
}
