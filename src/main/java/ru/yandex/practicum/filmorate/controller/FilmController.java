package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.debug("Creat film {}", film);

        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.debug("Update film {}", film);

        return filmService.update(film);
    }

    @GetMapping
    public List<Film> getAll() {
        List<Film> films = filmService.getAll();
        log.debug("Get all films {}", films.size());

        return films;
    }

    @GetMapping("/{id}")
    public Film get(@PathVariable long id) {
        log.debug("Get film id:{}", id);

        return filmService.get(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        log.debug("Delete film id:{}", id);
        filmService.delete(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        log.debug("Add like film id:{} from user id:{}", id, userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable long id, @PathVariable long userId) {
        log.debug("Remove like film id:{} from user id:{}", id, userId);
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilmByGenreAndYear(
            @RequestParam(required = false, defaultValue = "10") int count,
            @RequestParam(value = "genreId", required = false, defaultValue = "0") int genreId,
            @RequestParam(value = "year", required = false, defaultValue = "0") int year) {
        log.debug("Get popular films with genre:{} & year:{}, count:{} ", genreId, year, count);

        return filmService.getPopularFilmByGenreAndYear(count, genreId, year);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> get(@PathVariable long directorId, @RequestParam(defaultValue = "year") String sortBy) {
        log.debug("Get films by director={} and sort by={}", directorId, sortBy);

        return filmService.getByDirector(directorId, sortBy);
    }
  
    @GetMapping("/common")
    public List<Film> getCommonFilm(
            @RequestParam(value = "userId") long id,
            @RequestParam(value = "friendId") long otherId) {
        log.debug("Get common films user id:{} & user id:{}", id, otherId);

        return filmService.getCommonFilm(id, otherId);
    }

    @GetMapping("/search")
    public List<Film> searchFilms(
            @RequestParam(value = "query") String query,
            @RequestParam(value = "by") String by) {
        log.debug("Search films query:{} & by:{}", query, by);

        return filmService.searchFilms(query, by);
    }

}
