package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.Optional;

public interface ReviewStorage {

    Review save(Review review);

    Optional<Review> find(long id);

    Review update(Review review);

    void delete(long id);

}
