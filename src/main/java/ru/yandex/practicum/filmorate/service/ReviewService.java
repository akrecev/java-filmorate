package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewStorage reviewStorage;
    private final FilmService filmService;
    private final UserService userService;

    @Autowired
    public ReviewService(ReviewStorage reviewStorage, FilmService filmService, UserService userService) {
        this.reviewStorage = reviewStorage;
        this.filmService = filmService;
        this.userService = userService;
    }

    public Review create(Review review) {
        return reviewStorage.save(review);
    }

    public Review get(long id) {
        return find(id);
    }

    public List<Review> getAllReviews(int count) {
        return reviewStorage.findAllReviews(count);
    }

    public List<Review> getFilmAllReviews(long filmId, int count) {
        return reviewStorage.findFilmAllReviews(filmId, count);
    }

    public Review update(Review review) {
        find(review.getId());
        return reviewStorage.update(review);
    }

    public void delete(long id) {
        reviewStorage.delete(id);
    }

    public void addLike(long id, long userId) {
        find(id);
        userService.find(userId);
        reviewStorage.addLike(id, userId);
    }

    public void addDislike(long id, long userId) {
    }

    public void deleteLike(long id, long userId) {
    }

    public void deleteDislike(long id, long userId) {
    }

    private Review find(long id) {
       return reviewStorage.find(id).orElseThrow(() -> new DataNotFoundException("id=" + id));
    }
}
