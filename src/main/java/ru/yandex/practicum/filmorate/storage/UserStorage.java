package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage{

    User save(User user);

    User update(User user);

    User get(long id);

    void delete(long id);

    List<User> getAll();

}
