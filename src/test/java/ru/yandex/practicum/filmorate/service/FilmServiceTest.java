package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.LikesStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.db.FilmDbStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmServiceTest {

    FilmStorage filmStorage;
    private final LikesStorage likesStorage;

    FilmService filmService;
    UserService userService;
    private final JdbcTemplate jdbcTemplate;
    Film film;
    private MpaStorage mpaStorage;
    private GenreStorage genreStorage;

    FilmServiceTest(LikesStorage likesStorage, JdbcTemplate jdbcTemplate) {
        this.likesStorage = likesStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    void setUp() {
        filmStorage = new FilmDbStorage(jdbcTemplate);
        filmService = new FilmService(filmStorage, userService, likesStorage, mpaStorage, genreStorage);
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