package ru.yandex.practicum.filmorate.storage.impl.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest // указываем, о необходимости подготовить бины для работы с БД
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class DbUserStorageImplTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void add_shouldCreateUser() {
        DbUserStorageImpl dbUserStorageImpl = new DbUserStorageImpl(jdbcTemplate);

        User newUser = addDemoUser(dbUserStorageImpl);

        User savedUser = dbUserStorageImpl.getById(newUser.getId()).orElse(null);

        assertThat(savedUser)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(newUser);        // и сохраненного пользователя - совпадают
    }

    @Test
    public void update_shouldEditUser() {
        DbUserStorageImpl dbUserStorageImpl = new DbUserStorageImpl(jdbcTemplate);

        User newUser = addDemoUser(dbUserStorageImpl);

        User updatingUser = newUser.toBuilder().login("vanya123_updated").build();
        dbUserStorageImpl.update(updatingUser);

        User updatedUser = dbUserStorageImpl.getById(newUser.getId()).orElse(null);
        assert updatedUser != null;

        Assert.isTrue(updatedUser.getLogin().equals("vanya123_updated"), "Не ожидаемое значение login");

        assertThat(updatingUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(updatedUser);
    }

    @Test
    public void delete_shouldDeleteUser() {
        DbUserStorageImpl dbUserStorageImpl = new DbUserStorageImpl(jdbcTemplate);

        User newUser = addDemoUser(dbUserStorageImpl);

        User savedUser = dbUserStorageImpl.getById(newUser.getId()).orElse(null);
        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser);

        assert savedUser != null;
        Long deletedUserId = dbUserStorageImpl.delete(savedUser.getId());

        User deletedUser = dbUserStorageImpl.getById(deletedUserId).orElse(null);

        assertThat(deletedUser)
                .isNull();
    }

    @Test
    void getAll_shouldReturnAllUsers() {
        DbUserStorageImpl dbUserStorageImpl = new DbUserStorageImpl(jdbcTemplate);
        addDemoUsers(dbUserStorageImpl);

        List<User> users = dbUserStorageImpl.getAll();

        Assert.isTrue(users.size() == 3, "Размер коллекции неверный");
        Assert.isTrue(users.get(0).getLogin().equals("vanya"), "Такое имя пользователя не ожидалось");
    }

    @Test
    void getAll_shouldReturnUserById() {
        DbUserStorageImpl dbUserStorageImpl = new DbUserStorageImpl(jdbcTemplate);

        User addedUser = addDemoUser(dbUserStorageImpl);

        User receivedUser = dbUserStorageImpl.getById(addedUser.getId()).orElse(null);

        assertThat(addedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(receivedUser);
    }

    private User addDemoUser(DbUserStorageImpl dbUserStorageImpl) {
        User newUser = User.builder()
                .login("vanya")
                .name("Ivan Ivanov")
                .email("vanya" + new Random().nextInt(100000) + "@gmail.ru")
                .birthday(LocalDate.of(1993, 1, 1))
                .build();

        return dbUserStorageImpl.add(newUser);
    }

    private void addDemoUsers(DbUserStorageImpl dbUserStorageImpl) {
        User newUser1 = User.builder()
                .login("vanya")
                .name("Ivan Ivanov")
                .email("vanya" + new Random().nextInt(100000) + "@gmail.ru")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        User newUser2 = User.builder()
                .login("petya")
                .name("Petr Petrov")
                .email("petya" + new Random().nextInt(100000) + "@gmail.ru")
                .birthday(LocalDate.of(1991, 1, 1))
                .build();

        User newUser3 = User.builder()
                .login("vasya")
                .name("Vasiliy Vasilyevich")
                .email("vasya" + new Random().nextInt(100000) + "@gmail.ru")
                .birthday(LocalDate.of(1992, 1, 1))
                .build();

        dbUserStorageImpl.add(newUser1);
        dbUserStorageImpl.add(newUser2);
        dbUserStorageImpl.add(newUser3);
    }


}