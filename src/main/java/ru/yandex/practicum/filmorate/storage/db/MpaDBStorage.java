package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class MpaDBStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    public MpaDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Mpa> find(int id) {
        final String sql = "SELECT * FROM MPA WHERE MPA_ID = ?";
        final List<Mpa> mpa = jdbcTemplate.query(sql, MpaDBStorage::mpaMapper, id);

        return Optional.ofNullable(mpa.isEmpty() ? null : mpa.get(0));
    }

    @Override
    public List<Mpa> getAll() {
        final String sql = "SELECT * FROM MPA";

        return jdbcTemplate.query(sql, MpaDBStorage::mpaMapper);
    }

    static Mpa mpaMapper(ResultSet rs, int rowNum) throws SQLException {
        return new Mpa(
                rs.getInt("MPA_ID"),
                rs.getString("MPA_NAME")
        );
    }

}
