package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.EntityActions;
import ru.yandex.practicum.filmorate.storage.UserActionsStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserActionsDbStorage implements UserActionsStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addAction(EntityActions entityActions) {

        final String sql = "INSERT INTO USER_ACTIONS(user_id, event_type, operation, entity_id, timestamp)" +
                "VALUES(?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {

            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"EVENT_ID"});

            stmt.setInt(1, (int) entityActions.getUserId());

            stmt.setString(2, entityActions.getEventType());

            stmt.setString(3, entityActions.getOperation());

            stmt.setInt(4, (int) entityActions.getEntityId());

            stmt.setLong(5, entityActions.getTimestamp());

            return stmt;
        }, keyHolder);
    }

    @Override
    public List<EntityActions> getNewsFeed(int userId) {

        return jdbcTemplate.query(
                "SELECT ua.* " +
                    "FROM USER_ACTIONS AS ua " +
                    "WHERE ua.user_id = ?", (resultSet, rowNum) -> mapEntityActions(resultSet), userId
        );
    }

    @Override
    public void removeAction(EntityActions entityActions) {

    }

    private EntityActions mapEntityActions(ResultSet resultSet) throws SQLException {

        return EntityActions.builder()
                .eventId(resultSet.getInt("event_id"))
                .userId(resultSet.getInt("user_id"))
                .entityId(resultSet.getInt("entity_id"))
                .eventType(resultSet.getString("event_type"))
                .operation(resultSet.getString("operation"))
                .timestamp(resultSet.getLong("timestamp"))
                .build();
    }
}
