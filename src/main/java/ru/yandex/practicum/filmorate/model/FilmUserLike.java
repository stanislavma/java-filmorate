package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;

/**
 * Film user like
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
}
