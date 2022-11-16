package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    Storage<User> storage;
    UserService userService;
    User user;

    @BeforeEach
    void setUp() {
        storage = new InMemoryUserStorage();
        userService = new UserService(storage);
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