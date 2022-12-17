package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface LikesStorage {

    void addLike(long filmId, long userId);

    void deleteLike(long filmId, long userId);

    public void deleteUserLikes(long userId);

    public void deleteFilmLikes(long filmId);

    List<Film> getPopular(int count);

}
