package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void beforeAll() {
        objectMapper.findAndRegisterModules();
    }

    @Test
    void add_shouldCreateUser() throws Exception {
        User user = User.builder()
                .email("first@gmail.com")
                .login("first")
                .name("Stanislav")
                .birthday(LocalDate.parse("1987-11-24"))
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @Test
    void getAll_shouldReturnAllUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3))); // Проверка, что добавлено 3 пользователя
    }

    @Test
    void update_shouldEditUser() throws Exception {
        User user = User.builder()
                .email("second@gmail.com")
                .login("second")
                .name("Stanislav")
                .birthday(LocalDate.parse("1987-11-24"))
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());

        User updatingUser = User.builder()
                .id(2L)
                .email("second_updated@gmail.com")
                .login("sm_updated")
                .name("Stanislav_updated")
                .birthday(LocalDate.parse("1987-11-24"))
                .build();

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatingUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Stanislav_updated"));

    }

    @Test
    void add_shouldBadRequest_notValidEmailFormat() throws Exception {
        User user = User.builder()
                .email("sm")
                .login("sm_validation_email")
                .name("Stanislav")
                .birthday(LocalDate.parse("1987-11-24"))
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void add_shouldBadRequest_loginIsNull() throws Exception {
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
    void add_shouldBadRequest_loginIsEmpty() throws Exception {
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
    void getAll_loginShouldBeUsedIfNameIsEmpty() throws Exception {
        User user = User.builder()
                .email("third@gmail.com")
                .login("third")
                .birthday(LocalDate.parse("1987-11-24"))
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("third"));
    }

    @Test
    void add_shouldBadRequest_birthdayInFutureTime() throws Exception {
        User user = User.builder()
                .email("forth@gmail.com")
                .login("forth")
                .name("Stanislav")
                .birthday(LocalDate.parse("2555-11-24"))
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

}