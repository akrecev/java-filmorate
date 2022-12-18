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

    public User create(User user) {
        throwBadRequest(user);
        setNameIfEmpty(user);
        return userStorage.save(user);
    }

    public User get(long id) {
        return find(id);
    }

    public List<User> getAll() {
        return userStorage.findAll();
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

    public void deleteAll() {
        userStorage.deleteAll();
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
    }

    public void removeFriend(long id, long friendId) {
        find(id);
        find(friendId);
        friendStorage.removeFriend(id, friendId);
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

    public Boolean getStatusFriendship(long id, long friendId) {
        find(id);
        find(friendId);

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

    User find(long id) {
        return userStorage.find(id).orElseThrow(() -> new DataNotFoundException("id:" + id));
    }


}
