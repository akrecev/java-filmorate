package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.debug("Creat user {}", user);
        return userService.save(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.debug("Update user {}", user);
        return userService.update(user);
    }

    @GetMapping
    public List<User> getUsers() {
        List<User> users = userService.getAll();
        log.debug("Get all users {}", users.size());
        return users;
    }

    @GetMapping("/{id}")
    public User get(@PathVariable long id) {
        log.debug("Get user id:{}", id);
        return userService.get(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.debug("User id:{} add friend id:{}", id, friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable long id, @PathVariable long friendId) {
        log.debug("User id:{} remove friend id:{}", id, friendId);
        userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable long id) {
        List<User> friends = userService.getFriends(id);
        log.debug("Get user id:{} friends", id);
        return friends;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        List<User> commonFriends = userService.getCommonFriends(id, otherId);
        log.debug("Get common friends user id:{} & user id:{}", id, otherId);
        return commonFriends;
    }


}
