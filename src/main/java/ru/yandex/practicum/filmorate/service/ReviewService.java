package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.EntityActions;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.UserActionsStorage;

import java.sql.Timestamp;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewStorage reviewStorage;
    private final UserService userService;

    private final UserActionsStorage userActionsStorage;

    @Autowired
    public ReviewService(ReviewStorage reviewStorage, UserService userService, UserActionsStorage userActionsStorage) {
        this.reviewStorage = reviewStorage;
        this.userService = userService;
        this.userActionsStorage = userActionsStorage;
    }

    public Review create(Review review) {

        throwException(review);

        review = reviewStorage.save(review);

        userActionsStorage.addAction(
                EntityActions.builder()
                        .eventId(0)
                        .userId(review.getUserId())
                        .entityId(review.getReviewId())
                        .eventType("REVIEW")
                        .operation("ADD")
                        .timestamp(new Timestamp(System.currentTimeMillis()).getTime())
                        .build()
        );

        return review;
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

        review = reviewStorage.update(review);

        userActionsStorage.addAction(
                EntityActions.builder()
                        .eventId(0)
                        .userId(review.getUserId())
                        .entityId(review.getReviewId())
                        .eventType("REVIEW")
                        .operation("UPDATE")
                        .timestamp(new Timestamp(System.currentTimeMillis()).getTime())
                        .build()
        );

        return review;
    }

    public void delete(long id) {

        Review review = find(id);

        userActionsStorage.addAction(
                EntityActions.builder()
                        .eventId(0)
                        .userId(review.getUserId())
                        .entityId(review.getReviewId())
                        .eventType("REVIEW")
                        .operation("REMOVE")
                        .timestamp(new Timestamp(System.currentTimeMillis()).getTime())
                        .build()
        );

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