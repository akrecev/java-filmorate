package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    static User userMapper(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("USER_ID"));
        user.setEmail(rs.getString("EMAIL"));
        user.setLogin(rs.getString("LOGIN"));
        user.setName(rs.getString("USER_NAME"));
        user.setBirthday(rs.getDate("BIRTH_DAY").toLocalDate());
        return user;
    }

    @Override
    public Optional<User> save(User user) {
        String sql = "INSERT INTO USERS(EMAIL, LOGIN, USER_NAME, BIRTH_DAY) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"USER_ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            final LocalDate birthday = user.getBirthday();
            if (birthday == null) {
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, Date.valueOf(birthday));
            }
            return stmt;
        }, keyHolder);
        final long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        user.setId(id);
        return find(id);
    }

    @Override
    public Optional<User> update(User updateUser) {
        final String sql = "UPDATE USERS SET EMAIL = ?, LOGIN = ?, USER_NAME = ?, BIRTH_DAY = ? WHERE USER_ID = ?";
        jdbcTemplate.update(sql, updateUser.getEmail(), updateUser.getLogin(), updateUser.getName(),
                updateUser.getBirthday(), updateUser.getId());
        return find(updateUser.getId());
    }

    @Override
    public Optional<User> find(long id) {
        final String sql = "SELECT * FROM USERS WHERE USER_ID = ?";
        final List<User> users = jdbcTemplate.query(sql, UserDbStorage::userMapper, id);
        return Optional.ofNullable(users.isEmpty() ? null : users.get(0));
    }

    @Override
    public void delete(long id) {
        final String sql = "DELETE FROM USERS WHERE USER_ID = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<User> getAll() {
        final String sql = "SELECT * FROM USERS";
        return jdbcTemplate.query(sql, UserDbStorage::userMapper);
    }
}
