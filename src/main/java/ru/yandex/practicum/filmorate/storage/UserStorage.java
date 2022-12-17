package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage{

    User save(User user);

    Optional<User> find(long id);

    List<User> findAll();

    User update(User user);

    void delete(long id);

    void deleteAll();

}
