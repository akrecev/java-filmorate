package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.List;

@Service
public class DirectorService {

    private final DirectorStorage directorStorage;

    public DirectorService(DirectorStorage directorStorage) {
        this.directorStorage = directorStorage;
    }

    public Director create(Director director) {
        throwBadRequest(director);
        directorStorage.save(director);
        return get(director.getId());
    }

    public Director get(long id) {
        return find(id);
    }

    public List<Director> getAll() {
        return directorStorage.getAll();
    }

    public Director update(Director director) {
        throwBadRequest(director);
        find(director.getId());

        return directorStorage.update(director);
    }

    public void delete(long id) {
        directorStorage.delete(id);
    }

    public void throwBadRequest(Director director) {
        if (director.getName() == null || director.getName().isBlank()) {
            throw new BadRequestException("Invalid director name");
        }
    }

    private Director find(long id) {
        return directorStorage.find(id).orElseThrow(() -> new DataNotFoundException("id:" + id));
    }

}
