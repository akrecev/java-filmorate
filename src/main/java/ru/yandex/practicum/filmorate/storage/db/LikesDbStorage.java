package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.LikesStorage;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class LikesDbStorage implements LikesStorage {

    private final JdbcTemplate jdbcTemplate;

    public LikesDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(long filmId, long userId) {
        final String sql = "MERGE INTO LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
        updateRate(filmId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        final String sql = "DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sql, filmId, userId);
        updateRate(filmId);
    }

    @Override
    public List<Film> getPopularFilmByGenreAndYear(int count, int genreId, int year) {

        final String popularFilmsByYear = "SELECT * FROM FILMS F, MPA M " +
                "WHERE F.MPA_ID = M.MPA_ID AND YEAR(RELEASE_DATE) = ? " +
                "ORDER BY RATE DESC LIMIT ?";

        final String popularFilmsByGenre = "SELECT * FROM FILMS F, MPA M " +
                "INNER JOIN FILM_GENRES AS G ON G.FILM_ID = F.FILM_ID " +
                "WHERE F.MPA_ID = M.MPA_ID AND G.GENRE_ID = ? " +
                "ORDER BY RATE DESC LIMIT ?";

        final String popularFilmsByYearAndGenre = "SELECT * FROM FILMS F, MPA M " +
                "INNER JOIN FILM_GENRES AS G ON G.FILM_ID = F.FILM_ID " +
                "WHERE F.MPA_ID = M.MPA_ID AND G.GENRE_ID = ? AND YEAR(RELEASE_DATE) = ?" +
                "ORDER BY RATE DESC LIMIT ?";

        final String popularFilmsByCount = "SELECT * FROM FILMS F, MPA M " +
                "WHERE F.MPA_ID = M.MPA_ID " +
                "ORDER BY RATE DESC LIMIT ?";

        if (genreId == 0 & year == 0) {
            return jdbcTemplate.query(popularFilmsByCount, FilmDbStorage::filmMapper, count);
        }
        if (genreId == 0) {
            return jdbcTemplate.query(popularFilmsByYear, FilmDbStorage::filmMapper, year, count);

        }
        if (year == 0) {
            return jdbcTemplate.query(popularFilmsByGenre, FilmDbStorage::filmMapper, genreId, count);
        }
        return jdbcTemplate.query(popularFilmsByYearAndGenre, FilmDbStorage::filmMapper, genreId, year, count);
    }

    @Override
    public List<Film> getFilmsRecommendationsFor(long id) {
        String sqlGetLikesFilmUser = "SELECT user_id, count(user_id) count_user\n" +
                " FROM likes\n" +
                " WHERE film_id IN (SELECT DISTINCT(film_id)\n" +
                "                   FROM likes\n" +
                "                   WHERE user_id = ?)\n" +
                "   AND user_id <> ?\n" +
                " GROUP BY user_id;";

        SqlRowSet usersWithCountRows = jdbcTemplate.queryForRowSet(sqlGetLikesFilmUser, id, id);

        Map<Long, Long> UsersAll = new HashMap<>();
        while (usersWithCountRows.next()) {
            UsersAll.put(usersWithCountRows.getLong("user_id"),
                    usersWithCountRows.getLong("count_user"));
        }

        final Optional<Long> maxCountCommonLikes = UsersAll.values().stream().max(Long::compareTo);

        List<Long> userForRecommends = UsersAll.entrySet().stream()
                .filter(user-> user.getValue().equals(maxCountCommonLikes.get()))
                .filter(user-> user.getKey() != id)
                .map(user -> user.getKey()).collect(Collectors.toList());

        if (userForRecommends.isEmpty()) {
            return new ArrayList<>();
        }

        String joinUsers = userForRecommends.stream().map(String::valueOf)
                .collect(Collectors.joining(", "));

        String res = "SELECT * FROM FILMS F, MPA M\n" +
                "WHERE F.mpa_id = M.mpa_id AND\n" +
                "F.film_id IN (SELECT film_id\n" +
                "               FROM LIKES\n" +
                "               WHERE F.film_id IN\n" +
                "                       (SELECT DISTINCT(film_id)\n" +
                "                       FROM likes\n" +
                "                       WHERE user_id IN (?) AND\n" +
                "                           film_id NOT IN\n" +
                "                           (SELECT DISTINCT(film_id)\n" +
                "                           FROM likes WHERE user_id=?)))\n" +
                "ORDER BY RATE;";

        return jdbcTemplate.query(res, FilmDbStorage::filmMapper, joinUsers, id);
    }

    private void updateRate(long filmId) {
        final String sql = "UPDATE FILMS F SET RATE = (" +
                "SELECT COUNT(L.USER_ID) FROM LIKES L WHERE L.FILM_ID = F.FILM_ID" +
                ") WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, filmId);
    }

}
