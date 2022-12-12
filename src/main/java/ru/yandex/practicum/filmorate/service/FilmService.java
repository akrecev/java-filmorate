package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.LikesStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Service
public class FilmService {

    private final static LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, Month.DECEMBER, 28);
    private final UserService userService;
    private final FilmStorage filmStorage;
    private final LikesStorage likesStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService, LikesStorage likesStorage, MpaStorage mpaStorage, GenreStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.likesStorage = likesStorage;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }

    public Film save(Film film) {
        validate(film);
        filmStorage.save(film);
        film.setMpa(mpaStorage.get(film.getMpa().getId()));
        genreStorage.load(List.of(film));
        return film;
    }

    public Film get(long id) {
        final Film film = filmStorage.get(id);
        genreStorage.load(List.of(film));
        return film;
    }

    public List<Film> getAll() {
        final List<Film> allFilms = filmStorage.getAll();
        genreStorage.load(allFilms);
        return allFilms;
    }

    public Film update(Film film) {
        final long id = film.getId();
        if (filmStorage.get(id) == null) {
            throw new DataNotFoundException("id=" + id);
        }
        validate(film);
        filmStorage.update(film);
        film.setMpa(mpaStorage.get(film.getMpa().getId()));
        genreStorage.load(List.of(film));
        return film;
    }

    public void validate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new BadRequestException("Invalid film name");
        }
        if (film.getDescription() == null || film.getDescription().isBlank()) {
            throw new BadRequestException("Invalid film description");
        }
        if (film.getReleaseDate() == null) {
            throw new BadRequestException("Invalid film release date");
        }
        if (film.getReleaseDate().isBefore(CINEMA_BIRTHDAY)) {
            throw new BadRequestException("Release date cannot be earlier than December 28, 1895");
        }
        if (film.getReleaseDate().isAfter(LocalDate.now())) {
            throw new BadRequestException("Release date cannot be in the future");
        }
        if (film.getDuration() <= 0) {
            throw new BadRequestException("Invalid film duration");
        }
    }

    public void addLike(long filmId, long userId) {
        validate(filmStorage.get(filmId));
        userService.validate(userService.get(userId));
        likesStorage.addLike(filmId, userId);
    }

    public void removeLike(long filmId, long userId) {
        validate(filmStorage.get(filmId));
        userService.validate(userService.get(userId));
        likesStorage.removeLike(filmId, userId);
    }

    public List<Film> getPopular(int count) {
        return likesStorage.getPopular(count);
    }

}
