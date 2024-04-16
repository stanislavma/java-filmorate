package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

@Component
@RequiredArgsConstructor
@Slf4j
public class DbGenreStorageImpl implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Genre> getAll() {
        String sql = "SELECT id, name FROM genre ORDER BY id";
        Collection<Genre> genres = new ArrayList<>();

        try {
            genres = jdbcTemplate.query(sql, this::mapRow);
        } catch (DataAccessException e) {
            log.error("Error retrieving all Genres", e);
        }

        return genres;
    }

    @Override
    public Genre getById(long id) {
        try {
            String sqlQuery = "select id, name " +
                    "from genre where id = ?";

            return jdbcTemplate.queryForObject(sqlQuery, this::mapRow, id);
        } catch (DataAccessException e) {
            log.error("Error in getById", e);
        }

        return null;
    }

    private Genre mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .build();
    }

}
