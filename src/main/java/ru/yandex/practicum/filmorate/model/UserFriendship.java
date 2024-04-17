package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;

/**
 * User friendship
 */
@Data
@Jacksonized
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class UserFriendship {
    private Long id;
    private final Long userIdOne;
    private final Long userIdTwo;
}
