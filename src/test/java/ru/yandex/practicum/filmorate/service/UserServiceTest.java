package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.db.UserDbStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private final JdbcTemplate jdbcTemplate;
    UserStorage userStorage;
    UserService userService;
    FriendStorage friendStorage;
    User user;

    UserServiceTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    void setUp() {
        userStorage = new UserDbStorage(jdbcTemplate);
        userService = new UserService(userStorage, friendStorage);
        user = new User();
        user.setEmail("adress@email.com");
        user.setLogin("loginUser");
        user.setName("nameUser");
        user.setBirthday(LocalDate.now().minusYears(40));
    }

    @Test
    void validUser() {
        assertDoesNotThrow(() -> userService.validate(user));
    }

    @Test
    void validateEmail() {
        user.setEmail(null);
        assertThrows(BadRequestException.class, () -> userService.validate(user));

        user.setEmail("");
        assertThrows(BadRequestException.class, () -> userService.validate(user));
    }

    @Test
    void validateLogin() {
        user.setLogin(null);
        assertThrows(BadRequestException.class, () -> userService.validate(user));

        user.setLogin("");
        assertThrows(BadRequestException.class, () -> userService.validate(user));
    }

    @Test
    void validateLoginWithSpace() {
        user.setLogin("q q");
        assertThrows(BadRequestException.class, () -> userService.validate(user));
    }

    @Test
    void validateBirthday() {
        user.setBirthday(LocalDate.now().plusDays(1));
        assertThrows(BadRequestException.class, () -> userService.validate(user));
    }

}