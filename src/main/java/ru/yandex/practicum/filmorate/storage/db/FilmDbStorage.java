package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
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

        return find(id).get();
    }

    @Override
    public Optional<Film> find(long id) {
        final String sql = "SELECT * FROM FILMS F, MPA M WHERE F.MPA_ID = M.MPA_ID AND F.FILM_ID = ?";
        final List<Film> films = jdbcTemplate.query(sql, FilmDbStorage::filmMapper, id);

        return Optional.ofNullable(films.isEmpty() ? null : films.get(0));
    }

    @Override
    public List<Film> findAll() {
        final String sql = "SELECT * FROM FILMS F, MPA M WHERE F.MPA_ID = M.MPA_ID";

        return jdbcTemplate.query(sql, FilmDbStorage::filmMapper);
    }

    @Override
    public Film update(Film updateFilm) {
        final String sql = "UPDATE FILMS SET FILM_NAME = ?, DESCRIPTION = ?, DURATION = ?, RELEASE_DATE = ?, " +
                "RATE = ?, MPA_ID = ? WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, updateFilm.getName(), updateFilm.getDescription(), updateFilm.getDuration(),
                updateFilm.getReleaseDate(), updateFilm.getRate(), updateFilm.getMpa().getId(), updateFilm.getId());
        saveGenres(updateFilm);

        return find(updateFilm.getId()).get();
    }

    @Override
    public void delete(long id) {
        final String sql = "DELETE FROM FILMS WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void deleteAll() {
        final String sql ="DELETE FROM FILMS";
        jdbcTemplate.update(sql);
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

}
