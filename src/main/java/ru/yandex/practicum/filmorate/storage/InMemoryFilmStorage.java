package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

@Component
public class InMemoryFilmStorage extends AbstractDataStorage<Film> implements FilmStorage {
}
