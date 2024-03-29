package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film save(Film film) {
        final String sql = "INSERT INTO FILMS(FILM_NAME, DESCRIPTION, DURATION, RELEASE_DATE, RATE, MPA_ID)" +
                "VALUES(?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setInt(3, film.getDuration());
            stmt.setDate(4, Date.valueOf(film.getReleaseDate()));
            stmt.setLong(5, film.getRate());
            stmt.setInt(6, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        final long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        film.setId(id);
        saveGenres(film);
        saveDirectors(film);

        return find(id).get();
    }

    @Override
    public Film update(Film updateFilm) {
        final String sql = "UPDATE FILMS SET FILM_NAME = ?, DESCRIPTION = ?, DURATION = ?, RELEASE_DATE = ?, " +
                "MPA_ID = ? WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, updateFilm.getName(), updateFilm.getDescription(), updateFilm.getDuration(),
                updateFilm.getReleaseDate(), updateFilm.getMpa().getId(), updateFilm.getId());
        saveGenres(updateFilm);
        saveDirectors(updateFilm);

        return find(updateFilm.getId()).get();
    }

    @Override
    public Optional<Film> find(long id) {
        final String sql = "SELECT * FROM FILMS F, MPA M WHERE F.MPA_ID = M.MPA_ID AND F.FILM_ID = ?";
        final List<Film> films = jdbcTemplate.query(sql, FilmDbStorage::filmMapper, id);

        return Optional.ofNullable(films.isEmpty() ? null : films.get(0));
    }

    @Override
    public void delete(long id) {
        final String sql = "DELETE FROM FILMS WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Film> getAll() {
        final String sql = "SELECT * FROM FILMS F, MPA M WHERE F.MPA_ID = M.MPA_ID";

        return jdbcTemplate.query(sql, FilmDbStorage::filmMapper);
    }

    static Film filmMapper(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("FILM_ID"));
        film.setName(rs.getString("FILM_NAME"));
        film.setDescription(rs.getString("DESCRIPTION"));
        film.setDuration(rs.getInt("DURATION"));
        film.setReleaseDate(rs.getDate("RELEASE_DATE").toLocalDate());
        film.setRate(rs.getLong("RATE"));
        film.setMpa(new Mpa(rs.getInt("MPA.MPA_ID"), rs.getString("MPA.MPA_NAME")));

        return film;
    }

    private void saveGenres(Film film) {
        final long filmId = film.getId();
        jdbcTemplate.update("DELETE FROM FILM_GENRES WHERE FILM_ID = ?", filmId);
        final LinkedHashSet<Genre> genres = film.getGenres();
        if (genres == null || genres.isEmpty()) {
            return;
        }
        final List<Genre> genreList = new LinkedList<>(genres);
        jdbcTemplate.batchUpdate(
                "MERGE INTO FILM_GENRES(FILM_ID, GENRE_ID) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, filmId);
                        ps.setInt(2, genreList.get(i).getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return genreList.size();
                    }
                }
        );
    }
    @Override
    public List<Film> getCommonFilm(long id, long otherId) {
        final String sql = "SELECT * FROM FILMS AS F " +
                "INNER JOIN LIKES AS A ON A.FILM_ID = F.FILM_ID " +
                "INNER JOIN LIKES AS B ON B.FILM_ID = F.FILM_ID " +
                "INNER JOIN MPA AS С ON С.MPA_ID = F.MPA_ID " +
                "WHERE A.USER_ID = ? AND B.USER_ID = ?" +
                "ORDER BY RATE DESC";

        return jdbcTemplate.query(sql, FilmDbStorage::filmMapper, id, otherId);
    }

    private void saveDirectors(Film film) {
        final long filmId = film.getId();
        jdbcTemplate.update("DELETE FROM FILM_DIRECTORS WHERE FILM_ID = ?", filmId);
        final LinkedHashSet<Director> directors = film.getDirectors();
        if (directors == null || directors.isEmpty()) {
            return;
        }
        final List<Director> directorList = new LinkedList<>(directors);
        jdbcTemplate.batchUpdate(
                "MERGE INTO FILM_DIRECTORS(FILM_ID, DIRECTOR_ID) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, filmId);
                        ps.setLong(2, directorList.get(i).getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return directorList.size();
                    }
                }
        );
    }

    @Override
    public List<Film> searchFilmsByTitle(String query) {
        String searchRequest = "%" + query + "%";
        final String sql = "SELECT * FROM FILMS F " +
                "LEFT JOIN MPA M ON F.MPA_ID = M.MPA_ID " +
                "WHERE LOWER( F.FILM_NAME ) LIKE ? " +
                "ORDER BY F.RATE DESC";

        return jdbcTemplate.query(sql, FilmDbStorage::filmMapper, searchRequest);
    }

    @Override
    public List<Film> searchFilmsByDirectorName(String query) {
        String searchRequest = "%" + query + "%";
        final String sql = "SELECT * FROM FILMS F " +
                "LEFT JOIN MPA M ON F.MPA_ID = M.MPA_ID " +
                "LEFT JOIN FILM_DIRECTORS FD ON FD.FILM_ID = F.FILM_ID " +
                "LEFT JOIN DIRECTORS D ON D.DIRECTOR_ID = FD.DIRECTOR_ID " +
                "WHERE LOWER( D.DIRECTOR_NAME ) LIKE ? " +
                "ORDER BY F.RATE DESC";

        return jdbcTemplate.query(sql, FilmDbStorage::filmMapper, searchRequest);
    }

    @Override
    public List<Film> searchFilmsByTitleOrDirectorName(String query) {
        String searchRequest = "%" + query + "%";
        final String sql = "SELECT * FROM FILMS F " +
                "LEFT JOIN MPA M ON F.MPA_ID = M.MPA_ID " +
                "LEFT JOIN FILM_DIRECTORS FD ON FD.FILM_ID = F.FILM_ID " +
                "LEFT JOIN DIRECTORS D ON D.DIRECTOR_ID = FD.DIRECTOR_ID " +
                "WHERE ( LOWER( F.FILM_NAME ) LIKE ? OR LOWER( D.DIRECTOR_NAME ) LIKE ?)" +
                "ORDER BY F.RATE DESC";

        return jdbcTemplate.query(sql, FilmDbStorage::filmMapper, searchRequest, searchRequest);
    }
}
