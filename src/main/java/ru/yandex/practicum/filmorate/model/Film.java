package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Film extends StorageData {

    @NotBlank
    private String name;

    @Size(min = 1, max = 200)
    private String description;

    @PastOrPresent
    private LocalDate releaseDate;

    @Positive
    private int duration;

    @JsonIgnore
    private Set<Long> userLikes = new HashSet<>();

    @JsonIgnore
    private long rate = 0L;

    public void addLike(long userId) {
        userLikes.add(userId);
        rate = userLikes.size();
    }

    public void removeLike(long userId) {
        userLikes.remove(userId);
        rate = userLikes.size();
    }

    @Override
    public String toString() {
        return "Film{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", releaseDate=" + releaseDate +
                ", duration=" + duration +
                ", userLikes=" + userLikes +
                ", rate=" + rate +
                ", id=" + id +
                '}';
    }
}
