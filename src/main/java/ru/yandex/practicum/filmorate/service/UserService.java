package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.EntityActions;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserStorage userStorage;
    private final FriendStorage friendStorage;
    private final LikesStorage likesStorage;
    private final GenreStorage genreStorage;

    private final UserActionsStorage userActionsStorage;

    public User create(User user) {
        throwBadRequest(user);
        setNameIfEmpty(user);
        return userStorage.save(user);
    }

    public User get(long id) {
        return find(id);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User update(User user) {
        throwBadRequest(user);
        setNameIfEmpty(user);
        find(user.getId());

        return userStorage.update(user);
    }

    public void delete(long id) {
        userStorage.delete(id);
    }


    public void throwBadRequest(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new BadRequestException("Invalid user email");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new BadRequestException("Invalid user login");
        }
        if (user.getLogin().contains(" ")) {
            throw new BadRequestException("User login contains space");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new BadRequestException("Invalid user birthday");
        }
    }

    public void addFriend(long id, long friendId) {
        if (id == friendId) {
            throw new BadRequestException("Adding yourself as a friend");
        }
        find(id);
        find(friendId);
        friendStorage.addFriend(id, friendId);

        userActionsStorage.addAction(
                EntityActions.builder()
                        .eventId(0)
                        .userId(id)
                        .entityId(friendId)
                        .eventType("FRIEND")
                        .operation("ADD")
                        .timestamp(new Timestamp(System.currentTimeMillis()).getTime())
                        .build()
        );
    }

    public void removeFriend(long id, long friendId) {
        find(id);
        find(friendId);
        friendStorage.removeFriend(id, friendId);

        userActionsStorage.addAction(
                EntityActions.builder()
                        .eventId(0)
                        .userId(id)
                        .entityId(friendId)
                        .eventType("FRIEND")
                        .operation("REMOVE")
                        .timestamp(new Timestamp(System.currentTimeMillis()).getTime())
                        .build()
        );
    }

    public List<User> getFriends(long id) {
        find(id);

        return friendStorage.getFriends(id);
    }

    public List<User> getCommonFriends(long id, long otherId) {
        find(id);
        find(otherId);

        return friendStorage.getCommonFriends(id, otherId);
    }

    public List<Film> getFilmsRecommendationsFor(long id) {
        find(id);

        var allFilms = likesStorage.getFilmsRecommendationsFor(id);
        genreStorage.load(allFilms);

        return allFilms;
    }

    private void setNameIfEmpty(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    User find(long id) {
        return userStorage.find(id).orElseThrow(() -> new DataNotFoundException("id:" + id));
    }

    public List<EntityActions> getUserFeed(int userId) {

        find(userId);

        return userActionsStorage.getNewsFeed(userId);
    }
}
