package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage{

    Optional<User> save(User user);

    Optional<User> update(User user);

    Optional<User> find(long id);

    void delete(long id);

    List<User> getAll();

}
