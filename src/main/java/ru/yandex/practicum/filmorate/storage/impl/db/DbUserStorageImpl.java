package ru.yandex.practicum.filmorate.storage.impl.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DbUserStorageImpl implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User add(User user) {
        try {
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("app_user")
                    .usingGeneratedKeyColumns("id");
            long userId = simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue();
            user.setId(userId);
        } catch (Exception e) {
            log.error("Error in add user", e);
        }

        return user;
    }

    @Override
    public User update(User user) {
        try {
            String sqlQuery = "update app_user set " +
                    " login = ?, name = ?, email = ?, birthday = ?" +
                    " where id = ?";

            jdbcTemplate.update(sqlQuery,
                    user.getLogin(),
                    user.getName(),
                    user.getEmail(),
                    user.getBirthday(),
                    user.getId());
        } catch (DataAccessException e) {
            log.error("Error in update user", e);
        }

        return user;
    }

    @Override
    public Long delete(long id) {
        try {
            String sqlQuery = "delete from app_user where id = ?";
            boolean isSuccess = jdbcTemplate.update(sqlQuery, id) > 0;

        } catch (DataAccessException e) {
            log.error("Error in delete user", e);
        }

        return id;
    }

    @Override
    public List<User> getAll() {
        try {
            String sql = "select id, login, name, email, birthday " +
                    "FROM app_user u;";

            return jdbcTemplate.query(sql, this::mapRow);
        } catch (DataAccessException e) {
            log.error("Error in getAll", e);
        }

        return null;
    }

    @Override
    public User getById(long id) {
        try {
            String sql = "select id, login, name, email, birthday " +
                    "FROM app_user u " +
                    "WHERE id = ?";

            return jdbcTemplate.queryForObject(sql, this::mapRow, id);
        } catch (DataAccessException e) {
            log.error("Error in getById", e);
        }

        return null;
    }

    private User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = null;
        try {
            user = User.builder()
                    .id(rs.getLong("id"))
                    .login(rs.getString("login"))
                    .name(rs.getString("name"))
                    .email(rs.getString("email"))
                    .birthday(rs.getDate("birthday").toLocalDate())
                    .build();

        } catch (RuntimeException e) {
            log.error("Error in mapRow", e);
        }

        return user;
    }

}
