package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewStorage {

    Review save(Review review);

    Optional<Review> find(long id);

    List<Review> findFilmAllReviews(long filmId, int count);

    List<Review> findAllReviews(int count);

    Review update(Review review);

    void delete(long id);

    void addLike(long id, long userId);

    void addDislike(long id, long userId);

    void deleteLike(long id, long userId);

    void deleteDislike(long id, long userId);
}