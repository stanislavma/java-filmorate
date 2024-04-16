package ru.yandex.practicum.filmorate.model;

import lombok.*;


@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Genre {
    private final Long id;
    private String name;

}