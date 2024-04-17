package ru.yandex.practicum.filmorate.storage.impl.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class DbFilmStorageImpl implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film add(Film film) {
        try {
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("film")
                    .usingGeneratedKeyColumns("id");
            long filmId = simpleJdbcInsert.executeAndReturnKey(toFilmMap(film)).longValue();
            film.setId(filmId);

            // Добавление жанров фильма в таблицу film_genre
            if (film.getGenres() != null && !film.getGenres().isEmpty()) {
                simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                        .withTableName("film_genre")
                        .usingColumns("film_id", "genre_id");

                for (Genre genre : film.getGenres()) {
                    Map<String, Object> parameters = new HashMap<>();
                    parameters.put("film_id", filmId);
                    parameters.put("genre_id", genre.getId());
                    simpleJdbcInsert.execute(parameters);
                }
            }

        } catch (Exception e) {
            log.error("Error in add film", e);
        }

        return film;
    }

    @Override
    public Film update(Film film) {
        try {
            String sqlQuery = "update film set " +
                    " name = ?, description = ?, release_date = ?, duration = ?, mpa = ? " +
                    " where id = ?";

            // Обновляем возрастной рейтинг
            Long mpaId = film.getMpa() != null ? film.getMpa().getId() : null;

            jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    mpaId,
                    film.getId());

            // Удаление жанров фильма
            String sqlDeleteGenres = "DELETE FROM film_genre WHERE film_id = ?";
            jdbcTemplate.update(sqlDeleteGenres, film.getId());

            // Добавление жанров фильма
            if (film.getGenres() != null && !film.getGenres().isEmpty()) {
                SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                        .withTableName("film_genre")
                        .usingColumns("film_id", "genre_id");

                for (Genre genre : film.getGenres()) {
                    Map<String, Object> parameters = new HashMap<>();
                    parameters.put("film_id", film.getId());
                    parameters.put("genre_id", genre.getId());
                    simpleJdbcInsert.execute(parameters);
                }
            }

        } catch (DataAccessException e) {
            log.error("Error in update film", e);
        }

        return film;
    }

    @Override
    public Long delete(long id) {
        try {
            String sqlQuery = "delete from film where id = ?";
            boolean isSuccess = jdbcTemplate.update(sqlQuery, id) > 0;

        } catch (DataAccessException e) {
            log.error("Error in delete film", e);
        }

        return id;
    }

    @Override
    public List<Film> getAll() {
        try {
            String sql = "SELECT f.id, f.name, f.description, f.release_date, f.duration, f.mpa, mpa.name as mpa_name, " +
                    "STRING_AGG(g.id, ', ') AS genres_ids, " +
                    "STRING_AGG(g.name, ', ') AS genres_names " +
                    "FROM film f " +
                    "LEFT JOIN film_genre fg ON f.id = fg.film_id " +
                    "LEFT JOIN genre g ON fg.genre_id = g.id " +
                    "LEFT JOIN mpa ON mpa.id = f.mpa " +
                    "GROUP BY f.id, f.name, f.description, f.release_date, f.duration, f.mpa, mpa.name " +
                    "ORDER BY f.id;";

            return jdbcTemplate.query(sql, this::mapRow);
        } catch (DataAccessException e) {
            log.error("Error in getAll", e);
        }

        return null;
    }

    @Override
    public Optional<Film> getById(long id) {
        try {
            String sqlQuery = "select f.id, f.name, f.description, f.release_date, f.duration, f.mpa, mpa.name as mpa_name, " +
                    "STRING_AGG(g.id, ', ') AS genres_ids, " +
                    "STRING_AGG(g.name, ', ') AS genres_names " +
                    "                    from film f " +
                    "                             LEFT JOIN film_genre fg ON f.id = fg.film_id " +
                    "                             LEFT JOIN genre g ON fg.genre_id = g.id " +
                    "                             LEFT JOIN mpa ON mpa.id = f.mpa " +
                    "                    where f.id = ? " +
                    "GROUP BY f.id, f.name, f.description, f.release_date, f.duration, f.mpa, mpa.name ";

            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, this::mapRow, id));
        } catch (DataAccessException e) {
            log.error("Error in getById", e);
        }

        return Optional.empty();
    }

    private Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = null;
        try {
            film = Film.builder()
                    .id(rs.getLong("id"))
                    .name(rs.getString("name"))
                    .description(rs.getString("description"))
                    .releaseDate(rs.getDate("release_date").toLocalDate())
                    .duration(rs.getShort("duration"))
                    .build();

            Long mpaId = rs.getObject("mpa", Long.class);
            if (mpaId != null && mpaId != 0) {
                String mpaName = rs.getString("mpa_name");
                Mpa mpa = new Mpa(mpaId, mpaName);
                film.setMpa(mpa);
            } else {
                film.setMpa(null);  // Устанавливаем mpa как null, если не найден в базе данных
            }

            String genresIdsString = rs.getString("genres_ids");
            String genresNamesString = rs.getString("genres_names");

            if (genresIdsString != null && genresNamesString != null &&
                    !genresIdsString.isEmpty() && !genresNamesString.isEmpty()) {
                String[] genreIds = genresIdsString.split(", ");
                String[] genreNames = genresNamesString.split(", ");

                Set<Genre> genres = new HashSet<>();
                for (int i = 0; i < genreIds.length; i++) {
                    if (i < genreNames.length) {
                        long genreId = Long.parseLong(genreIds[i]);
                        String genreName = genreNames[i];
                        genres.add(new Genre(genreId, genreName));
                    }
                }
                film.setGenres(genres);
            }

            // Загрузка лайков
            Set<Long> likes = getLikesForFilm(rs.getLong("id"));
            film.setLikes(likes);

        } catch (RuntimeException e) {
            log.error("Error in mapRow", e);
        }

        return film;
    }

    private Set<Long> getLikesForFilm(Long filmId) {
        String sql = "SELECT user_id FROM film_user_like WHERE film_id = ?";
        return new LinkedHashSet<>(jdbcTemplate.queryForList(sql, Long.class, filmId));
    }

    public Map<String, Object> toFilmMap(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", film.getReleaseDate());
        values.put("duration", film.getDuration());

        if (film.getMpa() != null && film.getMpa().getId() != null) {
            values.put("mpa", film.getMpa().getId());
        }

        values.put("genres", film.getGenres());
        values.put("likes", film.getLikes());
        return values;
    }

}
