package ru.yandex.practicum.filmorate.storage.impl.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmUserLike;
import ru.yandex.practicum.filmorate.storage.FilmUserLikeStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class DbFilmUserLikeStorageImpl implements FilmUserLikeStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Long> getLikes(Long filmId) {
        String sql = "SELECT user_id FROM film_user_like WHERE film_id = ?";

        List<Long> userIds = new ArrayList<>();

        try {
            userIds = jdbcTemplate.query(sql,
                    (rs, rowNum) -> rs.getLong("user_id"),
                    filmId);
        } catch (DataAccessException e) {
            log.error("Error in getLikes", e);
        }

        return userIds;
    }

    @Override
    public Long addLike(Long filmId, Long userId) {
        Long filmUserLikeId = null;
        try {

            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("film_user_like")
                    .usingGeneratedKeyColumns("id")
                    .usingColumns("film_id", "user_id");

            filmUserLikeId = simpleJdbcInsert.executeAndReturnKey(
                    toFilmUserLikeMap(new FilmUserLike(filmId, userId))).longValue();

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

    public Map<String, Object> toFilmUserLikeMap(FilmUserLike filmUserLike) {
        Map<String, Object> values = new HashMap<>();
        values.put("film_id", filmUserLike.getFilmId());
        values.put("user_id", filmUserLike.getUserId());

        return values;
    }

    private FilmUserLike mapUserLikeRow(ResultSet rs, int rowNum) throws SQLException {
        return FilmUserLike.builder()
                .userId(rs.getLong("user_id"))
                .build();
    }

}
