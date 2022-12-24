package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Film save(Film film);

    Film update(Film film);

    Optional<Film> find(long id);

    void delete(long id);

    List<Film> getAll();

    List<Film> getCommonFilm(long id, long otherId);

}
