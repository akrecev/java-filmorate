package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MpaService {

    private final MpaStorage mpaStorage;

    public Mpa get(int id) {
        return mpaStorage.find(id).orElseThrow(() -> new DataNotFoundException("id=" + id));
    }

    public List<Mpa> getAll() {
        return mpaStorage.getAll();
    }

}
