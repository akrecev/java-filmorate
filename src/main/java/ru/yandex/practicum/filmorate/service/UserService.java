package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserService {

    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    @Autowired
    public UserService(UserStorage userStorage, FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

    public User save(User user) {
        setNameIfEmpty(user);
        return userStorage.save(user);
    }

    public User get(long id) {
        return userStorage.get(id);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User update(User user) {
        final long id = user.getId();
        if (userStorage.get(id) == null) {
            throw new DataNotFoundException("id=" + id);
        }
        setNameIfEmpty(user);
        validate(user);
        return userStorage.update(user);
    }

    public void delete(long id) {
        userStorage.delete(id);
    }

    public void validate(User user) {
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
        final User user = userStorage.get(id);
        final User friend = userStorage.get(friendId);
        validate(user);
        validate(friend);
        friendStorage.addFriend(id, friendId);
    }

    public void removeFriend(long id, long friendId) {
        final User user = userStorage.get(id);
        final User friend = userStorage.get(friendId);
        validate(user);
        validate(friend);
        friendStorage.removeFriend(id, friendId);
    }

    public List<User> getFriends(long id) {
        final User user = userStorage.get(id);
        validate(user);
        return friendStorage.getFriends(id);
    }

    public List<User> getCommonFriends(long id, long otherId) {
        final User user = userStorage.get(id);
        final User otherUser = userStorage.get(otherId);
        validate(user);
        validate(otherUser);
        return friendStorage.getCommonFriends(id, otherId);
    }

    public Boolean getStatusFriendship(long id, long friendId) {
        final User user = userStorage.get(id);
        final User friend = userStorage.get(friendId);
        validate(user);
        validate(friend);
        return friendStorage.getStatusFriendship(id, friendId);
    }

    private void setNameIfEmpty(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }


}
