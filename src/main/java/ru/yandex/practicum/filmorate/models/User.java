package ru.yandex.practicum.filmorate.models;

import lombok.Data;


import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {

    private int id;

    @NotNull
    @Email
    private String email;

    @NotBlank
    private String login;

    private String name;

    @PastOrPresent
    private LocalDate birthday;

}
