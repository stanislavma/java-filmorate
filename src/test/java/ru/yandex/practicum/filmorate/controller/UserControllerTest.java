package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void beforeAll() {
        objectMapper.findAndRegisterModules();
    }

    @Test
    void shouldAdd() throws Exception {
        User user = User.builder()
                .email("sm@gmail.com")
                .login("sm")
                .name("Stanislav")
                .birthday(LocalDate.parse("1987-11-24"))
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdate() throws Exception {
        shouldAdd();

        User user = User.builder()
                .id(1)
                .email("sm999@gmail.com")
                .login("sm999")
                .name("Stanislav999")
                .birthday(LocalDate.parse("1987-11-24"))
                .build();

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Stanislav999"));

    }

    @Test
    void shouldGet() throws Exception {
        shouldAdd();

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1))); // Проверка, что добавлен 1 пользователь
    }

    @Test
    void emailShouldBeValid() throws Exception {
        User user = User.builder()
                .email("sm")
                .login("sm")
                .name("Stanislav")
                .birthday(LocalDate.parse("1987-11-24"))
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginShouldNotBeNull() throws Exception {
        User user = User.builder()
                .email("sm@gmail.com")
                .name("Stanislav")
                .birthday(LocalDate.parse("1987-11-24"))
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginShouldNotBeEmpty() throws Exception {
        User user = User.builder()
                .email("sm@gmail.com")
                .login("")
                .name("Stanislav")
                .birthday(LocalDate.parse("1987-11-24"))
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginShouldBeUseIfNameIsEmpty() throws Exception {
        User user = User.builder()
                .email("sm@gmail.com")
                .login("sm")
                .birthday(LocalDate.parse("1987-11-24"))
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("sm"));
    }

    @Test
    void birthdayShouldBeInPastTime() throws Exception {
        User user = User.builder()
                .email("sm@gmail.com")
                .login("sm")
                .name("Stanislav")
                .birthday(LocalDate.parse("2555-11-24"))
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

}