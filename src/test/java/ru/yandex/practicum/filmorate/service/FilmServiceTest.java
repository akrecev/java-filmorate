package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmServiceTest {

    Storage<Film> storage;
    FilmService filmService;
    UserService userService;
    Film film;

    @BeforeEach
    void setUp() {
        storage = new InMemoryFilmStorage();
        filmService = new FilmService(storage, userService);
        film = new Film();
        film.setName("filmName");
        film.setDescription("filmDescription");
        film.setReleaseDate(LocalDate.now().minusYears(20));
        film.setDuration(120);
    }

    @Test
    void validFilm() {
        assertDoesNotThrow(() -> filmService.validate(film));
    }

    @Test
    void validateName() {
        film.setName(null);
        assertThrows(BadRequestException.class, () -> filmService.validate(film));

        film.setName("");
        assertThrows(BadRequestException.class, () -> filmService.validate(film));
    }

    @Test
    void validateDescription() {
        film.setDescription(null);
        assertThrows(BadRequestException.class, () -> filmService.validate(film));

        film.setDescription("");
        assertThrows(BadRequestException.class, () -> filmService.validate(film));
    }

    @Test
    void validateReleaseDate() {
        film.setReleaseDate(null);
        assertThrows(BadRequestException.class, () -> filmService.validate(film));

        film.setReleaseDate(LocalDate.now().plusDays(1));
        assertThrows(BadRequestException.class, () -> filmService.validate(film));

        film.setReleaseDate(LocalDate.MIN);
        assertThrows(BadRequestException.class, () -> filmService.validate(film));
    }

    @Test
    void validateDuration() {
        film.setDuration(0);
        assertThrows(BadRequestException.class, () -> filmService.validate(film));
    }

}