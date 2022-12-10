package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MpaDBStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    public MpaDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    static Mpa mpaMapper(ResultSet rs, int rowNum) throws SQLException {
        return new Mpa(
                rs.getInt("MPA_ID"),
                rs.getString("MPA_NAME")
        );
    }

    @Override
    public Mpa get(int id) {
        final String sql = "SELECT * FROM MPA WHERE MPA_ID = ?";
        final List<Mpa> mpa = jdbcTemplate.query(sql, MpaDBStorage::mpaMapper, id);
        if (mpa.isEmpty()) {
            throw new DataNotFoundException("id=" + id);
        }
        return mpa.get(0);
    }

    @Override
    public List<Mpa> getAll() {
        final String sql = "SELECT * FROM MPA";
        return jdbcTemplate.query(sql, MpaDBStorage::mpaMapper);
    }
}
