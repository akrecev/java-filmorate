package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ReviewDbStorage implements ReviewStorage {

    private final JdbcTemplate jdbcTemplate;

    public ReviewDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Review save(Review review) {
        final String sql = "INSERT INTO REVIEWS(POSITIVE, HEADER, CONTENT, FILM_ID, RATE) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"REVIEW_ID"});
            stmt.setBoolean(1, review.isPositive());
            stmt.setString(2, review.getHeader());
            stmt.setString(3, review.getContent());
            stmt.setLong(4, review.getFilmId());
            stmt.setLong(5, review.getRate());
            return stmt;
        }, keyHolder);
        final long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        review.setId(id);

        return review;
    }

    @Override
    public Optional<Review> find(long id) {
        final String sql = "SELECT * FROM REVIEWS WHERE REVIEW_ID = ?";
        final List<Review> reviews = jdbcTemplate.query(sql, ReviewDbStorage::reviewMapper, id);

        return Optional.ofNullable(reviews.isEmpty() ? null : reviews.get(0));
    }

    @Override
    public Review update(Review review) {
        final String sql = "UPDATE REVIEWS SET POSITIVE = ?, HEADER = ?, CONTENT = ?, FILM_ID = ?, RATE = ? " +
                "WHERE REVIEW_ID = ?";
        jdbcTemplate.update(sql, review.isPositive(), review.getHeader(), review.getContent(), review.getFilmId(),
                review.getRate(), review.getId());

        return find(review.getId()).get();
    }

    @Override
    public void delete(long id) {
        final String sql = "DELETE FROM REVIEWS WHERE REVIEW_ID = ?";
        jdbcTemplate.update(sql, id);
    }

    static Review reviewMapper(ResultSet rs, int rowNum) throws SQLException {
        return new Review(
                rs.getLong("REVIEW_ID"),
                rs.getBoolean("POSITIVE"),
                rs.getString("HEADER"),
                rs.getString("CONTENT"),
                rs.getLong("FILM_ID"),
                rs.getLong("RATE")
        );
    }

}
