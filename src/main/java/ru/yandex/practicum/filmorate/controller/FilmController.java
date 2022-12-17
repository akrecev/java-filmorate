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

    @GetMapping("/{id}")
    public Film get(@PathVariable long id) {
        log.debug("Get film id:{}", id);

        return filmService.get(id);
    }

    @GetMapping
    public List<Film> getAll() {
        List<Film> films = filmService.getAll();
        log.debug("Get all films {}", films.size());

        return films;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.debug("Update film {}", film);

        return filmService.update(film);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        log.debug("Delete film id:{}", id);
        filmService.delete(id);
    }

    @DeleteMapping
    public void deleteAll() {
        log.debug("Delete ALL films");
        filmService.deleteAll();
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
    public List<Film> getPopular(@RequestParam(defaultValue = "10") int count) {
        log.debug("Get popular films count={}", count);

        return filmService.getPopular(count);
    }

}
