package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * User
 */
@Data
@Jacksonized
@Builder(toBuilder = true)
public class User {
    private Long id;

    @NotNull
    @NotEmpty
    private String login;

    @Builder.Default
    private String name = "";

    @Email
    @NotNull
    private String email;

    @PastOrPresent
    private LocalDate birthday;

    @Builder.Default
    private Set<Long> friends = new LinkedHashSet<>();

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("login", login);
        values.put("name", name);
        values.put("email", email);
        values.put("birthday", birthday);

        values.put("friends", friends);

        return values;
    }

}
