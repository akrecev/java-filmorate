package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class GenreDBStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Genre> find(int id) {
        final String sql = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
        final List<Genre> genres = jdbcTemplate.query(sql, GenreDBStorage::genreMapper, id);

        return Optional.ofNullable(genres.isEmpty() ? null : genres.get(0));
    }

    public List<Genre> getFilmAllGenres(int filmId) {
        final String sql = "SELECT * FROM FILM_GENRES F, GENRES G WHERE F.GENRE_ID = G.GENRE_ID AND F.FILM_ID = ?";

        return jdbcTemplate.query(sql, GenreDBStorage::genreMapper, filmId);
    }

    @Override
    public List<Genre> getAll() {
        final String sql = "SELECT * FROM GENRES";

        return jdbcTemplate.query(sql, GenreDBStorage::genreMapper);
    }

    @Override
    public void load(List<Film> films) {
        String toSql = String.join(",", Collections.nCopies(films.size(), "?"));
        films.forEach(film -> film.getGenres().clear());
        final Map<Long, Film> filmMap = films.stream().collect(Collectors.toMap(Film::getId, Function.identity()));
        final String sql = "SELECT * FROM GENRES G, FILM_GENRES F " +
                "WHERE F.GENRE_ID = G.GENRE_ID AND FILM_ID IN(" + toSql + ") ";
        jdbcTemplate.query(sql, (rs) -> {
            final Film film = filmMap.get(rs.getLong("FILM_ID"));
            film.getGenres().add(genreMapper(rs, 0));
        }, films.stream().map(Film::getId).toArray());
    }

    static Genre genreMapper(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(
                rs.getInt("GENRE_ID"),
                rs.getString("GENRE_NAME")
        );
    }

}
