package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.EntityActions;

import java.util.List;

public interface UserActionsStorage {

    void addAction(EntityActions entityActions);

    List<EntityActions> getNewsFeed(int userId);

    void removeAction(EntityActions entityActions);

}
