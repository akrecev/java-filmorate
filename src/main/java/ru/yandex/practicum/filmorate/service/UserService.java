package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService extends AbstractService<User> {

    @Autowired
    public UserService(Storage<User> storage) {
        this.storage = storage;
    }

    @Override
    public User create(User user) {
        setNameIfEmpty(user);
        return super.create(user);
    }

    @Override
    public User update(User user) {
        setNameIfEmpty(user);
        return super.update(user);
    }

    @Override
    protected void validate(User user) {
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
        validate(storage.get(id));
        validate(storage.get(friendId));
        storage.get(id).addFriend(friendId);
        storage.get(friendId).addFriend(id);
    }

    public void removeFriend(long id, long friendId) {
        validate(storage.get(id));
        validate(storage.get(friendId));
        storage.get(id).removeFriend(friendId);
        storage.get(friendId).removeFriend(id);
    }

    public List<User> getFriends(long id) {
        validate(storage.get(id));
        User user = storage.get(id);
        List<Long> friendsIds = List.copyOf(user.getFriendsIds());
        List<User> friends = new ArrayList<>();
        friendsIds.forEach(friendId -> friends.add(storage.get(friendId)));
        return friends;
    }

    public List<User> getCommonFriends(long id, long otherId) {
        validate(storage.get(id));
        validate(storage.get(otherId));
        User user = storage.get(id);
        User otherUser = storage.get(otherId);
        List<Long> friendsIds = List.copyOf(user.getFriendsIds());
        List<User> commonFriends = new ArrayList<>();
        friendsIds.forEach(friendId -> {
            if (otherUser.getFriendsIds().contains(friendId)) {
                commonFriends.add(storage.get(friendId));
            }
        });
        return commonFriends;
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
