package ru.yandex.practicum.filmorate.storage.impl.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

@Component
@RequiredArgsConstructor
@Slf4j
public class DbMpaStorageImpl implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Mpa> getAll() {
        String sql = "SELECT id, name FROM mpa ORDER BY id";
        Collection<Mpa> mpas = new ArrayList<>();

        try {
            mpas = jdbcTemplate.query(sql, this::mapRow);
        } catch (DataAccessException e) {
            log.error("Error retrieving all mpa", e);
        }

        return mpas;
    }

    @Override
    public Mpa getById(long id) {
        try {
            String sqlQuery = "select id, name " +
                    "from mpa where id = ?";

            return jdbcTemplate.queryForObject(sqlQuery, this::mapRow, id);
        } catch (DataAccessException e) {
            log.error("Error in getById", e);
        }

        return null;
    }

    private Mpa mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .build();
    }

}
