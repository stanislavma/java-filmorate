package ru.yandex.practicum.filmorate.storage;

public interface FilmUserLikeStorage {

    Long addLike(Long filmId, Long userId);
    Long deleteLike(Long filmId, Long userId);

}
