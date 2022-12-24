package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreService {

    private final GenreStorage genreStorage;

    public Genre get(int id) {
        return genreStorage.find(id).orElseThrow(() -> new DataNotFoundException("id=" + id));
    }

    public List<Genre> getAll() {
        return genreStorage.getAll();
    }

}
