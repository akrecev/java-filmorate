package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.LikesStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Service
public class FilmService {

    private final static LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, Month.DECEMBER, 28);
    private final FilmStorage filmStorage;
    private final UserService userService;
    private final LikesStorage likesStorage;
    private final MpaService mpaService;
    private final GenreStorage genreStorage;
    private final DirectorStorage directorStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService, LikesStorage likesStorage,
                       MpaService mpaService, GenreStorage genreStorage, DirectorStorage directorStorage) {
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.likesStorage = likesStorage;
        this.mpaService = mpaService;
        this.genreStorage = genreStorage;
        this.directorStorage = directorStorage;
    }

    public Film create(Film film) {
        throwBadRequest(film);
        film = filmStorage.save(film);
        film.setMpa(mpaService.get(film.getMpa().getId()));
        genreStorage.load(List.of(film));
        directorStorage.load(List.of(film));

        return film;
    }

    public Film get(long id) {
        final Film film = find(id);
        genreStorage.load(List.of(film));
        directorStorage.load(List.of(film));

        return film;
    }

    public List<Film> getAll() {
        final List<Film> allFilms = filmStorage.getAll();
        genreStorage.load(allFilms);
        directorStorage.load(allFilms);

        return allFilms;
    }

    public Film update(Film film) {
        throwBadRequest(film);
        find(film.getId());
        Film updateFilm = filmStorage.update(film);
        updateFilm.setMpa(mpaService.get(updateFilm.getMpa().getId()));
        genreStorage.load(List.of(updateFilm));
        directorStorage.load(List.of(updateFilm));

        return updateFilm;
    }

    public void delete(long id) {
        filmStorage.delete(id);
    }

    public void throwBadRequest(Film film) {
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
        if (film.getDuration() <= 0) {
            throw new BadRequestException("Invalid film duration");
        }
    }

    public void addLike(long filmId, long userId) {
        throwBadRequest(find(filmId));
        userService.throwBadRequest(userService.get(userId));
        likesStorage.addLike(filmId, userId);
    }

    public void removeLike(long filmId, long userId) {
        throwBadRequest(find(filmId));
        userService.throwBadRequest(userService.get(userId));
        likesStorage.removeLike(filmId, userId);
    }

    public List<Film> getPopularFilmByGenreAndYear(int count, int genreId, int year) {
        final List<Film> films = likesStorage.getPopularFilmByGenreAndYear(count, genreId, year);
        genreStorage.load(films);
        directorStorage.load(films);

        return films;
    }
    public List<Film> getCommonFilm(long id, long otherId) {
        return filmStorage.getCommonFilm(id, otherId);
    }

    public List<Film> getByDirector(long directorId, String sortBy) {
        final List<Film> films;

        if(directorStorage.find(directorId).isEmpty()){
            throw new DataNotFoundException("Director not found");
        }

        if (sortBy.equals("likes")) {
            films = directorStorage.getDirectorFilmsByPopular(directorId);
        } else {
            films = directorStorage.getDirectorFilmsByYears(directorId);
        }

        genreStorage.load(films);
        directorStorage.load(films);

        return films;
    }



    private Film find(long id) {
        return filmStorage.find(id).orElseThrow(() -> new DataNotFoundException("id=" + id));
    }

}
