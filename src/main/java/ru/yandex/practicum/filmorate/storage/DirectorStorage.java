package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface DirectorStorage {
    Director save(Director director);

    Director update(Director director);

    Optional<Director> find(long id);

    void delete(long id);

    List<Director> getAll();

    void load(List<Film> films);

    List<Film> getDirectorFilmsByYears(long directorId);

    List<Film> getDirectorFilmsByPopular(long directorId);
}
