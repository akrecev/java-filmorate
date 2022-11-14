package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.BadRequestException;
import ru.yandex.practicum.filmorate.exceptions.ConflictException;
import ru.yandex.practicum.filmorate.models.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final static LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, Month.DECEMBER, 28);
    private final Map<Integer, Film> films = new HashMap<>();
    private int generateId = 1;

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        validateReleaseDate(film);
        int id = generateId++;
        film.setId(id);
        films.put(film.getId(), film);
        log.debug("Фильм {} добавлен", film.getName());
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            log.error("Фильм id: {}", film.getId());
            throw new ConflictException("Фильма с таким id не существует");
        }
        validateReleaseDate(film);
        for (Film currentFilm : films.values()) {
            if (film.getId() == currentFilm.getId()) {
                currentFilm.setName(film.getName());
                currentFilm.setDescription(film.getDescription());
                currentFilm.setReleaseDate(film.getReleaseDate());
                currentFilm.setDuration(film.getDuration());
                return film;
            }
        }
        films.put(film.getId(), film);
        log.debug("Фильм {} id: {} обновлен", film.getName(), film.getId());
        return film;
    }

    @GetMapping
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    public void validateReleaseDate(Film film) {
        if (film.getReleaseDate().isBefore(CINEMA_BIRTHDAY)) {
            log.error("Дата релиза: {}", film.getReleaseDate());
            throw new BadRequestException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
    }

}
