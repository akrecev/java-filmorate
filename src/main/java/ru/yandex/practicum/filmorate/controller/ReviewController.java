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

    @GetMapping("?filmId={filmId}&count={count}")
    private List<Review> getAllFilmReviews(@PathVariable long filmId, @PathVariable int count) {
        List<Review> reviews =  reviewService.getAllFilmReviews(filmId, count);
        log.debug("Get {} reviews for the film id:{}", reviews.size(), filmId);

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

}
