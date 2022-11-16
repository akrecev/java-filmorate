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
public class User extends StorageData {

    @NotNull
    @Email
    private String email;

    @NotBlank
    private String login;

    private String name;

    @PastOrPresent
    private LocalDate birthday;

    @JsonIgnore
    private Set<Long> friendsIds = new HashSet<>();

    public void addFriend(long friendId) {
        friendsIds.add(friendId);
    }

    public void removeFriend(long friendId) {
        friendsIds.remove(friendId);
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                ", friendsIds=" + friendsIds +
                ", id=" + id +
                '}';
    }
}