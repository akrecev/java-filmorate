package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.StorageData;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;

public abstract class AbstractService<T extends StorageData> {

    private long globalId = 1L;

    Storage<T> storage;

    protected abstract void validate(T data);

    public T create(T data) {
        validate(data);
        data.setId(globalId++);
        storage.create(data);
        return data;
    }

    public T update(T data) {
        validate(data);
        storage.update(data);
        return data;
    }

    public T get(long id) {
        T data = storage.get(id);
        if (data == null) {
            throw new DataNotFoundException(String.format("Data with id=%d not found", id));
        }
        return data;
    }

    public void delete(long id) {
        storage.delete(id);
    }

    public List<T> getAll() {
        List<T> allData = storage.getAll();
        if (allData == null) {
            throw new DataNotFoundException("Not found");
        }
        return allData;
    }

}
