package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;

public interface FilmUserLikeStorage {

    Collection<Long> getLikes(Long filmId);

    Long addLike(Long filmId, Long userId);

    Long deleteLike(Long filmId, Long userId);

}
