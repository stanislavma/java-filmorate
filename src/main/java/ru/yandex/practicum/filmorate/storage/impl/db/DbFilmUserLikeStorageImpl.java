package ru.yandex.practicum.filmorate.storage.impl.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmUserLike;
import ru.yandex.practicum.filmorate.storage.FilmUserLikeStorage;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class DbFilmUserLikeStorageImpl implements FilmUserLikeStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Long addLike(Long filmId, Long userId) {
        Long filmUserLikeId = null;
        try {

            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("film_user_like")
                    .usingGeneratedKeyColumns("id")
                    .usingColumns("film_id", "user_id");
            filmUserLikeId = simpleJdbcInsert.executeAndReturnKey(new FilmUserLike(filmId, userId).toMap()).longValue();

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("film_id", filmId);
            parameters.put("user_id", userId);
            simpleJdbcInsert.execute(parameters);

        } catch (Exception e) {
            log.error("Error in add addLike", e);
        }

        return filmUserLikeId;
    }

    @Override
    public Long deleteLike(Long filmId, Long userId) {
        try {
            String sqlQuery = "delete from film_user_like where film_id = ? and user_id = ?";
            boolean isSuccess = jdbcTemplate.update(sqlQuery, filmId, userId) > 0;

        } catch (DataAccessException e) {
            log.error("Error in deleteFriend", e);
        }

        return filmId;
    }

}
