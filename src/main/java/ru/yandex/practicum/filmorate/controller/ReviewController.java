package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/reviews")
@Slf4j
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    private Review create(@Valid @RequestBody Review review) {
        log.debug("Create review {}", review);

        return reviewService.create(review);
    }

    @GetMapping("/{id}")
    private Review get(@PathVariable long id) {
        log.debug("Get review id:{}", id);

        return reviewService.get(id);
    }

    @GetMapping()
    private List<Review> getFilmAllReviews(@RequestParam(defaultValue = "0") long filmId,
                                           @RequestParam(defaultValue = "10") int count) {
        if (filmId == 0) {
            List<Review> reviews = reviewService.getAllReviews(count);
            log.debug("Get {} reviews", reviews.size());

            return reviews;
        }
        List<Review> reviews = reviewService.getFilmAllReviews(filmId, count);
        log.debug("Get {} reviews for film id:{}", reviews.size(), filmId);

        return reviews;
    }

    @PutMapping
    public Review update(@Valid @RequestBody Review review) {
        log.debug("Update review {}", review);

        return reviewService.update(review);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        log.debug("Delete review id:{}", id);
        reviewService.delete(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        log.debug("User id:{} add like for review id:{}", userId, id);
        reviewService.addLike(id, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void addDislike(@PathVariable long id, @PathVariable long userId) {
        log.debug("User id: {} add dislike for review id:{}", userId, id);
        reviewService.addDislike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id, @PathVariable long userId) {
        log.debug("User id:{} add like for review id:{}", userId, id);
        reviewService.deleteLike(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void deleteDislike(@PathVariable long id, @PathVariable long userId) {
        log.debug("User id: {} add dislike for review id:{}", userId, id);
        reviewService.deleteDislike(id, userId);
    }

}
