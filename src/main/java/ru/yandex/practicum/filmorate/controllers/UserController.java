package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private int generateId = 1;

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        checkingLoginForSpace(user);
        fillingEmptyName(user);
        int id = generateId++;
        user.setId(id);
        users.put(user.getId(), user);
        log.debug("Пользователь " + user.getName() + " добавлен");
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            log.error("Пользователь id: " + user.getId());
            throw new ValidationException("Пользователя с таким id не существует");
        }
        checkingLoginForSpace(user);
        fillingEmptyName(user);
        for (User currentUser : users.values()) {
            if (user.getId() == currentUser.getId()) {
                currentUser.setEmail(user.getEmail());
                currentUser.setLogin(user.getLogin());
                currentUser.setName(user.getName());
                currentUser.setBirthday(user.getBirthday());
                return user;
            }
        }
        users.put(user.getId(), user);
        log.debug("Пользователь " + user.getName() + " id: " + user.getId() + " обновлен");
        return user;
    }

    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    private void checkingLoginForSpace(User user) {
        if (user.getLogin().contains(" ")) {
            log.error("Пользователь с логином: " + user.getLogin());
            throw new ValidationException("Логин не может содержать пробелы");
        }
    }

    private void fillingEmptyName(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

}
