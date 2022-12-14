package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.EntityActions;
import ru.yandex.practicum.filmorate.model.Film;
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

        return userService.create(user);
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

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        log.debug("Delete user id: {}", id);
        userService.delete(id);
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

    @GetMapping("/{id}/recommendations")
    public List<Film> getFilmsRecommendationsFor(@PathVariable long id) {
        List<Film> filmsRecommendations = userService.getFilmsRecommendationsFor(id);
        log.debug("Get films recommendations by user id:{}", id);

        return filmsRecommendations;
    }

    @GetMapping("/{id}/feed")
    public List<EntityActions> getUserFeed(@PathVariable("id") int userId) {

        log.debug("Get news feed for the user id:{}", userId);

        return userService.getUserFeed(userId);
    }

}
