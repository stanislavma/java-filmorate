package ru.yandex.practicum.filmorate.storage.impl.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest // указываем, о необходимости подготовить бины для работы с БД
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class DbFilmStorageImplTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void add_shouldCreateFilm() {
        DbFilmStorageImpl dbFilmStorageImpl = new DbFilmStorageImpl(jdbcTemplate);

        Film newFilm = addDemoFilm(dbFilmStorageImpl);

        Film savedFilm = dbFilmStorageImpl.getById(newFilm.getId());

        assertThat(savedFilm)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(newFilm);        // и сохраненного пользователя - совпадают
    }

    @Test
    public void update_shouldEditFilm() {
        DbFilmStorageImpl dbFilmStorageImpl = new DbFilmStorageImpl(jdbcTemplate);

        Film newFilm = addDemoFilm(dbFilmStorageImpl);

        Film updatingFilm = newFilm.toBuilder().name("Film № 1 updated").build();
        dbFilmStorageImpl.update(updatingFilm);

        Film updatedFilm = dbFilmStorageImpl.getById(newFilm.getId());

        Assert.isTrue(updatedFilm.getName().equals("Film № 1 updated"), "Не ожидаемое значение name");

        assertThat(updatingFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(updatedFilm);
    }

    @Test
    public void delete_shouldDeleteFilm() {
        DbFilmStorageImpl dbFilmStorageImpl = new DbFilmStorageImpl(jdbcTemplate);

        Film newFilm = addDemoFilm(dbFilmStorageImpl);

        Film savedFilm = dbFilmStorageImpl.getById(newFilm.getId());
        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newFilm);

        Long deletedFilmId = dbFilmStorageImpl.delete(savedFilm.getId());

        Film deletedFilm = dbFilmStorageImpl.getById(deletedFilmId);

        assertThat(deletedFilm)
                .isNull();
    }

    @Test
    void getAll_shouldReturnAllFilms() {
        DbFilmStorageImpl dbFilmStorageImpl = new DbFilmStorageImpl(jdbcTemplate);
        addDemoFilms(dbFilmStorageImpl);

        List<Film> films = dbFilmStorageImpl.getAll();

        Assert.isTrue(films.size() == 3, "Размер коллекции неверный");
        Assert.isTrue(films.get(0).getName().equals("Film № 1"), "Такое название фильма не ожидалось");
    }

    @Test
    void getAll_shouldReturnFilmById() {
        DbFilmStorageImpl dbFilmStorageImpl = new DbFilmStorageImpl(jdbcTemplate);

        Film addedFilm = addDemoFilm(dbFilmStorageImpl);

        Film receivedFilm = dbFilmStorageImpl.getById(addedFilm.getId());

        assertThat(addedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(receivedFilm);
    }

    private Film addDemoFilm(DbFilmStorageImpl dbFilmStorageImpl) {
        Film newFilm = Film.builder()
                .name("Film № 1")
                .description("Film № 1 description")
                .releaseDate(LocalDate.parse("2020-03-25"))
                .duration((short) 111)
                .build();

        return dbFilmStorageImpl.add(newFilm);
    }

    private void addDemoFilms(DbFilmStorageImpl dbFilmStorageImpl) {
        Film newFilm1 = Film.builder()
                .name("Film № 1")
                .description("Film № 1 description")
                .releaseDate(LocalDate.parse("2020-03-25"))
                .duration((short) 111)
                .build();

        Film newFilm2 = Film.builder()
                .name("Film № 2")
                .description("Film № 2 description")
                .releaseDate(LocalDate.parse("2021-03-25"))
                .duration((short) 112)
                .build();

        Film newFilm3 = Film.builder()
                .name("Film № 3")
                .description("Film № 3 description")
                .releaseDate(LocalDate.parse("2022-03-25"))
                .duration((short) 113)
                .build();

        dbFilmStorageImpl.add(newFilm1);
        dbFilmStorageImpl.add(newFilm2);
        dbFilmStorageImpl.add(newFilm3);
    }

}