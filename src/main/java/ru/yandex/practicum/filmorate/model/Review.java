package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class Review {

    private long reviewId;

    private String content;

    @NotNull
    private Boolean isPositive;

    private long userId;

    private long filmId;

    private long useful = 0L;

}