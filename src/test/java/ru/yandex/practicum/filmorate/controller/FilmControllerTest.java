package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void beforeAll() {
        objectMapper.findAndRegisterModules();
    }

    @Test
    void add_shouldCreateFilm() throws Exception {
        Film film = Film.builder()
                .name("Film № 1")
                .description("Film № 1")
                .releaseDate(LocalDate.parse("2020-03-25"))
                .duration((short) 112)
                .build();

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isOk());
    }

    @Test
    void getAll_shouldReturnAllFilms() throws Exception {
        mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2))); // Проверка, что добавлено 2 фильма
    }

    @Test
    void update_shouldEditFilm() throws Exception {
        add_shouldCreateFilm();

        Film film = Film.builder()
                .id(2L)
                .name("Film № 1")
                .description("Film № 1 description")
                .releaseDate(LocalDate.parse("2020-03-26"))
                .duration((short) 3333)
                .build();

        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.duration").value(3333));

    }

    @Test
    void add_shouldBadRequest_nameIsNull() throws Exception {
        Film film = Film.builder()
                .name("")
                .description("Film № 1")
                .releaseDate(LocalDate.parse("2020-03-25"))
                .duration((short) 127)
                .build();

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void add_shouldBadRequest_maxDescriptionLengthMore200() throws Exception {
        Film film = Film.builder()
                .name("Film № 1")
                .description("Film № 1 description Film № 1 description Film № 1 description Film № 1 description Film № 1 description Film № 1 description Film № 1 description Film № 1 description Film № 1 description Film № 1 description Film № 1 description Film № 1 description Film № 1 description Film № 1 description Film № 1 description Film № 1 description")
                .releaseDate(LocalDate.parse("2020-03-25"))
                .duration((short) 127)
                .build();

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void add_shouldBadRequest_releaseDateLater1895Year() throws Exception {
        Film film = Film.builder()
                .name("Film № 1")
                .description("Film № 1 description")
                .releaseDate(LocalDate.parse("1800-03-25"))
                .duration((short) 115)
                .build();

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void add_shouldBadRequest_durationIsNegative() throws Exception {
        Film film = Film.builder()
                .name("Film № 1")
                .description("Film № 1 description")
                .releaseDate(LocalDate.parse("2020-03-25"))
                .duration((short) -200)
                .build();

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }

}