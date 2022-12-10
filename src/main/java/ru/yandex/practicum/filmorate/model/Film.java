package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.LinkedHashSet;

@Getter
@Setter
@NoArgsConstructor
public class Film {

    public long rate = 0L;

    @NotBlank
    private String name;

    @Size(min = 1, max = 200)
    private String description;

    @Positive
    private int duration;
    Long id;
    @PastOrPresent
    private LocalDate releaseDate;
    private LinkedHashSet<Genre> genres = new LinkedHashSet<>();
    private Mpa mpa;

}
