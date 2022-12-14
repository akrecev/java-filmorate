package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.util.List;

@Component
public class FriendDBStorage implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;

    public FriendDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(long userId, long friendId) {
        final String sql = "MERGE INTO FRIENDS(USER_ID, FRIEND_ID) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        final String sql = "DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public List<User> getFriends(long userId) {
        final String sql = "SELECT * FROM USERS U, FRIENDS F WHERE U.USER_ID = F.FRIEND_ID AND F.USER_ID = ?";

        return jdbcTemplate.query(sql, UserDbStorage::userMapper, userId);
    }

    @Override
    public List<User> getCommonFriends(long userId, long otherId) {
        final String sql = "SELECT * FROM USERS U, FRIENDS F, FRIENDS O" +
                " WHERE U.USER_ID = F.FRIEND_ID AND U.USER_ID = O.FRIEND_ID AND F.USER_ID = ? AND O.USER_ID = ?";

        return jdbcTemplate.query(sql, UserDbStorage::userMapper, userId, otherId);
    }

    @Override
    public Boolean getStatusFriendship(long userId, long friendId) {
        final String sql = "SELECT * FROM FRIENDS F, FRIENDS O" +
                " WHERE F.USER_ID = O.FRIEND_ID AND O.USER_ID = F.FRIEND_ID AND F.USER_ID = ? AND O.USER_ID = ?";

        return !jdbcTemplate.queryForList(sql, userId, friendId).isEmpty();
    }
}
