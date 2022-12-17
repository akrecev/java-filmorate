package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Film save(Film film);

    Optional<Film> find(long id);

    List<Film> findAll();

    Film update(Film film);

    void delete(long id);

    void deleteAll();

}
