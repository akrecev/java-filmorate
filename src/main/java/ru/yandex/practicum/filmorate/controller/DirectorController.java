package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/directors")
@Slf4j
@RequiredArgsConstructor
public class DirectorController {
    private final DirectorService directorService;

    @PostMapping
    public Director create(@Valid @RequestBody Director director) {
        log.debug("Creat Director {}", director);

        return directorService.create(director);
    }

    @PutMapping
    public Director update(@Valid @RequestBody Director director) {
        log.debug("Update Director {}", director);

        return directorService.update(director);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        log.debug("Delete director id:{}", id);
        directorService.delete(id);
    }

    @GetMapping
    public List<Director> getDirectors() {
        List<Director> directors = directorService.getAll();
        log.debug("Get all directors {}", directors.size());

        return directors;
    }

    @GetMapping("/{id}")
    public Director get(@PathVariable long id) {
        log.debug("Get director id:{}", id);

        return directorService.get(id);
    }
}
