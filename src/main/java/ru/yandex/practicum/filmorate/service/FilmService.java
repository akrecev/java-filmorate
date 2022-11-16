package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.time.LocalDate;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService extends AbstractService<Film> {

    private final static LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, Month.DECEMBER, 28);
    private final static Comparator<Film> FILM_COMPARATOR = (o1, o2) -> (int) (o2.getRate() - o1.getRate());

    @Autowired
    public FilmService(Storage<Film> storage) {
        this.storage = storage;
    }

    @Override
    protected void validate(Film film) {
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
        if (film.getReleaseDate().isAfter(LocalDate.now())) {
            throw new BadRequestException("Release date cannot be in the future");
        }
        if (film.getDuration() <= 0) {
            throw new BadRequestException("Invalid film duration");
        }
    }

    public void addLike(long id, long userId) {
        validateById(id);
        if (userId <= 0) {
            throw new DataNotFoundException(String.format("Invalid user id:%d", userId));
        }
        storage.get(id).addLike(userId);
    }

    public void removeLike(long id, long userId) {
        validateById(id);
        if (userId <= 0) {
            throw new DataNotFoundException(String.format("Invalid user id:%d", userId));
        }
        storage.get(id).removeLike(userId);
    }

    public List<Film> getPopular(int count) {
        return storage.getAll().stream()
                .sorted(FILM_COMPARATOR)
                .limit(count)
                .collect(Collectors.toList());
    }

}
