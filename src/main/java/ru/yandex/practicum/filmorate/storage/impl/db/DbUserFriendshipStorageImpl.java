package ru.yandex.practicum.filmorate.storage.impl.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmUserLike;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserFriendshipStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class DbUserFriendshipStorageImpl implements UserFriendshipStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Long addUserToFriend(Long userIdOne, Long userIdTwo) {
        Long userFriendshipId = null;
        try {

            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("user_friendship")
                    .usingGeneratedKeyColumns("id")
                    .usingColumns("user_id_one", "user_id_two");
            userFriendshipId = simpleJdbcInsert.executeAndReturnKey(new FilmUserLike(userIdOne, userIdTwo).toMap()).longValue();

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("user_id_one", userIdOne);
            parameters.put("user_id_two", userIdTwo);
            simpleJdbcInsert.execute(parameters);

        } catch (Exception e) {
            log.error("Error in addUserToFriend", e);
        }

        return userFriendshipId;
    }

    @Override
    public Long deleteFriend(Long userIdOne, Long userIdTwo) {
        try {
            String sqlQuery = "delete from user_friendship where user_id_one = ? and user_id_two = ?";
            boolean isSuccess = jdbcTemplate.update(sqlQuery, userIdOne, userIdTwo) > 0;

        } catch (DataAccessException e) {
            log.error("Error in deleteFriend", e);
        }

        return userIdTwo;
    }

    @Override
    public Collection<User> getFriends(Long userId) {
        String sql = "SELECT u.* FROM app_user u JOIN user_friendship uf ON u.id = uf.user_id_two WHERE uf.user_id_one = ?";

        return jdbcTemplate.query(sql, this::mapUserRow, userId);
    }

    private User mapUserRow(ResultSet rs, int rowNum) throws SQLException {
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
            log.error("Error in mapUserRow", e);
        }

        return user;
    }

}
