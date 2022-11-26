package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

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
    private Map<Long, Boolean> friends = new HashMap<>();

    public void addFriend(long friendId) {
        friends.put(friendId, false);
    }

    public void confirmFriendship (long friendId) {
        friends.put(friendId, true);
    }

    public Boolean getStatusFriendship (long friendId) {
        return friends.get(friendId);
    }

    public void removeFriend(long friendId) {
        friends.remove(friendId);
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                ", friendsIds=" + friends +
                ", id=" + id +
                '}';
    }
}
