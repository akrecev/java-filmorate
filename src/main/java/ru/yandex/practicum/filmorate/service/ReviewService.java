package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewStorage reviewStorage;
    private final UserService userService;

    @Autowired
    public ReviewService(ReviewStorage reviewStorage, UserService userService) {
        this.reviewStorage = reviewStorage;
        this.userService = userService;
    }

    public Review create(Review review) {
        throwException(review);
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
        find(review.getReviewId());
        throwException(review);

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
        find(id);
        userService.find(userId);
        reviewStorage.addDislike(id, userId);
    }

    public void deleteLike(long id, long userId) {
        find(id);
        userService.find(userId);
        reviewStorage.deleteLike(id, userId);
    }

    public void deleteDislike(long id, long userId) {
        find(id);
        userService.find(userId);
        reviewStorage.deleteDislike(id, userId);
    }

    private Review find(long id) {
        return reviewStorage.find(id).orElseThrow(() -> new DataNotFoundException("id=" + id));
    }

    private void throwException(Review review) {
        if (review.getUserId() == 0) {
            throw new BadRequestException("userId=" + review.getUserId());
        }
        if (review.getUserId() < 0) {
            throw new DataNotFoundException("userId=" + review.getUserId());
        }
        if (review.getFilmId() == 0) {
            throw new BadRequestException("filmId=" + review.getFilmId());
        }
        if (review.getFilmId() < 0) {
            throw new DataNotFoundException("filmId=" + review.getFilmId());
        }
        if (review.getContent().isBlank()) {
            throw new BadRequestException("bad content: " + review.getUserId());
        }
    }
}