package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    private final FilmDbStorage filmStorage;

    @Test
    void filmDbStorageTest() {

        // testGet
        Film film = filmStorage.find(1).get();

        assertNotNull(film);
        assertEquals(film.getName(), "L’Arrivée d’un train en gare de la Ciotat", "Name does not match");
        assertEquals(film.getDescription(), "Train arriving at La Ciotat station",
                "Description does not match");
        assertEquals(film.getDuration(), 1, "Duration does not match");
        assertEquals(film.getReleaseDate(), LocalDate.of(1895, 12, 28),
                "ReleaseDate does not match");
        assertEquals(film.getRate(), 0, "Rate does not match");
        assertEquals(film.getMpa().getId(), 3, "Mpa does not match");

        // testGetAll
        List<Film> films = filmStorage.findAll();

        assertNotNull(films.get(0));
        assertEquals(films.get(0).getName(), "L’Arrivée d’un train en gare de la Ciotat",
                "Name does not match");
        assertEquals(films.get(0).getDescription(), "Train arriving at La Ciotat station",
                "Description does not match");
        assertEquals(films.get(0).getDuration(), 1, "Duration does not match");
        assertEquals(films.get(0).getReleaseDate(), LocalDate.of(1895, 12, 28),
                "ReleaseDate does not match");
        assertEquals(films.get(0).getRate(), 0, "Rate does not match");
        assertEquals(films.get(0).getMpa().getId(), 3, "Mpa does not match");

        assertNotNull(films.get(1));
        assertEquals(films.get(1).getName(), "Terminator 2: Judgment Day", "Name does not match");
        assertEquals(films.get(1).getDescription(), "Hasta la vista, baby",
                "Description does not match");
        assertEquals(films.get(1).getDuration(), 137, "Duration does not match");
        assertEquals(films.get(1).getReleaseDate(), LocalDate.of(1991, 12, 25),
                "ReleaseDate does not match");
        assertEquals(films.get(1).getRate(), 0, "Rate does not match");
        assertEquals(films.get(1).getMpa().getId(), 4, "Mpa does not match");

        IndexOutOfBoundsException indexOutOfBoundsException
                = Assertions.assertThrows(IndexOutOfBoundsException.class, () -> films.get(2),
                "IndexOutOfBoundsException was expected");
        Assertions.assertEquals("Index 2 out of bounds for length 2", indexOutOfBoundsException.getMessage());

        assertEquals(Optional.empty(), filmStorage.find(3));

        // testSave
        Film filmNew = new Film();
        filmNew.setName("Home Alone");
        filmNew.setDescription("Child trolls two bandits");
        filmNew.setDuration(103);
        filmNew.setReleaseDate(LocalDate.of(1990,11,10));
        filmNew.setRate(0);
        filmNew.setMpa(new Mpa(2, "PG"));

        filmStorage.save(filmNew);
        Film filmSave = filmStorage.find(3).get();

        assertNotNull(filmSave);
        assertEquals(filmSave.getName(), "Home Alone", "Name does not match");
        assertEquals(filmSave.getDescription(), "Child trolls two bandits", "Description does not match");
        assertEquals(filmSave.getDuration(), 103, "Duration does not match");
        assertEquals(filmSave.getReleaseDate(), LocalDate.of(1990,11,10),
                "ReleaseDate does not match");
        assertEquals(filmSave.getRate(), 0, "Rate does not match");
        assertEquals(filmSave.getMpa().getId(), 2, "Mpa does not match");

        // testUpdate
        Film filmBefore = filmStorage.find(3).get();
        filmBefore.setName("Home Alone 2: Lost in New York");
        filmBefore.setDescription("Child trolls two bandits again");
        filmBefore.setDuration(120);
        filmBefore.setReleaseDate(LocalDate.of(1992,11,20));

        filmStorage.update(filmBefore);
        Film filmAfter = filmStorage.find(3).get();
        assertEquals(filmAfter.getName(), "Home Alone 2: Lost in New York", "Name does not match");
        assertEquals(filmAfter.getDescription(), "Child trolls two bandits again",
                "Description does not match");
        assertEquals(filmAfter.getDuration(), 120, "Duration does not match");
        assertEquals(filmAfter.getReleaseDate(), LocalDate.of(1992,11,20),
                "ReleaseDate does not match");
        assertEquals(filmAfter.getRate(), 0, "Rate does not match");
        assertEquals(filmAfter.getMpa().getId(), 2, "Mpa does not match");

        // testDelete
        filmStorage.delete(1);
        assertEquals(Optional.empty(), filmStorage.find(1));

    }
}