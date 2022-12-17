package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Positive
    private long id;

    private boolean isPositive;

    @Size(min = 1, max = 255)
    private String header;

    @Size(min = 1, max = 5000)
    private String content;

    @Positive
    private long filmId;

    private long rate = 0L;

}
