package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class ReviewDbStorage implements ReviewStorage {

    private final JdbcTemplate jdbcTemplate;

    public ReviewDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Review save(Review review) {
        final String sql = "INSERT INTO REVIEWS(CONTENT, IS_POSITIVE, FILM_ID, USER_ID, USEFUL)" +
                " VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"REVIEW_ID"});
            stmt.setString(1, review.getContent());
            stmt.setBoolean(2, review.getIsPositive());
            stmt.setLong(3, review.getFilmId());
            stmt.setLong(4, review.getUserId());
            stmt.setLong(5, review.getUseful());
            return stmt;
        }, keyHolder);
        final long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        review.setReviewId(id);

        return find(id).get();
    }

    @Override
    public Optional<Review> find(long id) {
        final String sql = "SELECT * FROM REVIEWS WHERE REVIEW_ID = ?";
        final List<Review> reviews = jdbcTemplate.query(sql, ReviewDbStorage::reviewMapper, id);

        return Optional.ofNullable(reviews.isEmpty() ? null : reviews.get(0));
    }

    @Override
    public List<Review> findFilmAllReviews(long filmId, int count) {
        final String sql = "SELECT * FROM REVIEWS WHERE FILM_ID = ? ORDER BY USEFUL DESC LIMIT ?";

        return jdbcTemplate.query(sql, ReviewDbStorage::reviewMapper, filmId, count);
    }

    @Override
    public List<Review> findAllReviews(int count) {
        final String sql = "SELECT * FROM REVIEWS ORDER BY USEFUL DESC LIMIT ?";

        return jdbcTemplate.query(sql, ReviewDbStorage::reviewMapper, count);
    }

    @Override
    public Review update(Review review) {
        final String sql = "UPDATE REVIEWS SET CONTENT = ?, IS_POSITIVE = ? WHERE REVIEW_ID = ?";
        jdbcTemplate.update(sql, review.getContent(), review.getIsPositive(), review.getReviewId());

        return find(review.getReviewId()).get();
    }

    @Override
    public void delete(long id) {
        final String sql = "DELETE FROM REVIEWS WHERE REVIEW_ID = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void addLike(long id, long userId) {
        final String sql = "MERGE INTO REVIEW_LIKES(USER_ID, REVIEW_ID) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, id);
        rateCalculation(id);
    }

    @Override
    public void addDislike(long id, long userId) {
        final String sql = "MERGE INTO REVIEW_DISLIKES(USER_ID, REVIEW_ID) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, id);
        rateCalculation(id);
    }

    @Override
    public void deleteLike(long id, long userId) {
        final String sql = "DELETE FROM REVIEW_LIKES WHERE USER_ID = ? AND REVIEW_ID = ?";
        jdbcTemplate.update(sql, userId, id);
        rateCalculation(id);
    }

    @Override
    public void deleteDislike(long id, long userId) {
        final String sql = "DELETE FROM REVIEW_DISLIKES WHERE USER_ID = ? AND REVIEW_ID = ?";
        jdbcTemplate.update(sql, userId, id);
        rateCalculation(id);
    }

    private void rateCalculation(long id) {
        final String sql = "UPDATE REVIEWS R SET USEFUL = (" +
                "(SELECT COUNT(L.USER_ID) FROM REVIEW_LIKES L WHERE L.REVIEW_ID = ?)" +
                " - (SELECT COUNT(D.USER_ID) FROM REVIEW_DISLIKES D WHERE D.REVIEW_ID = ?)" +
                ") WHERE R.REVIEW_ID = ?";
        jdbcTemplate.update(sql, id, id, id);
    }

    static Review reviewMapper(ResultSet rs, int rowNum) throws SQLException {
        Review review = new Review();
        review.setReviewId(rs.getLong("REVIEW_ID"));
        review.setContent(rs.getString("CONTENT"));
        review.setIsPositive(rs.getBoolean("IS_POSITIVE"));
        review.setUserId(rs.getLong("USER_ID"));
        review.setFilmId(rs.getLong("FILM_ID"));
        review.setUseful(rs.getLong("USEFUL"));
        return review;
    }


}