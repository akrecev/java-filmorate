package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class DirectorDbStorage implements DirectorStorage {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void save(Director director) {
        String sql = "INSERT INTO DIRECTORS (DIRECTOR_NAME) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"DIRECTOR_ID"});
            stmt.setString(1, director.getName());
            return stmt;
        }, keyHolder);
        final long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        director.setId(id);
    }

    @Override
    public Director update(Director updateDirector) {
        final String sql = "UPDATE DIRECTORS SET DIRECTOR_NAME = ? WHERE DIRECTOR_ID = ?";
        jdbcTemplate.update(sql, updateDirector.getName(), updateDirector.getId());

        return find(updateDirector.getId()).get();
    }

    @Override
    public Optional<Director> find(long id) {
        final String sql = "SELECT * FROM DIRECTORS WHERE DIRECTOR_ID = ?";
        final List<Director> directors = jdbcTemplate.query(sql, DirectorDbStorage::directorMapper, id);

        return Optional.ofNullable(directors.isEmpty() ? null : directors.get(0));
    }

    @Override
    public void delete(long id) {
        final String sql = "DELETE FROM DIRECTORS WHERE DIRECTOR_ID = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Director> getAll() {
        final String sql = "SELECT * FROM DIRECTORS";

        return jdbcTemplate.query(sql, DirectorDbStorage::directorMapper);
    }

    @Override
    public void load(List<Film> films) {
        final String sql = "SELECT FD.FILM_ID, FD.DIRECTOR_ID, D.DIRECTOR_NAME FROM FILM_DIRECTORS FD " +
                "JOIN DIRECTORS D ON D.DIRECTOR_ID = FD.DIRECTOR_ID " +
                "WHERE FD.FILM_ID IN (:filmIds)";

        List<Long> filmdIds = films.stream()
                .map(Film::getId)
                .collect(Collectors.toList());

        Map<Long, Film> filmMap = films.stream()
                .collect(Collectors.toMap(Film::getId, film -> film, (a, b) -> b));

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("filmIds", filmdIds);
        SqlRowSet sqlRowSet = namedParameterJdbcTemplate.queryForRowSet(sql, sqlParameterSource);

        while (sqlRowSet.next()) {
            filmMap.get(sqlRowSet.getLong("FILM_ID")).getDirectors().add(new Director(
                    sqlRowSet.getLong("DIRECTOR_ID"),
                    sqlRowSet.getString("DIRECTOR_NAME")
            ));
        }

        films.forEach(film -> film.getDirectors().addAll(
                filmMap.get(film.getId()).getDirectors()
        ));
    }

    @Override
    public List<Film> getDirectorFilmsByYears(long directorId) {
        final String sql = "SELECT * FROM FILMS F, FILM_DIRECTORS FD, MPA M WHERE F.MPA_ID = M.MPA_ID AND F.FILM_ID = FD.FILM_ID AND FD.DIRECTOR_ID = ? ORDER BY F.RELEASE_DATE ASC";

        return jdbcTemplate.query(sql, FilmDbStorage::filmMapper, directorId);
    }

    @Override
    public List<Film> getDirectorFilmsByPopular(long directorId) {
        final String sql = "SELECT * FROM FILMS F, FILM_DIRECTORS FD, MPA M WHERE F.MPA_ID = M.MPA_ID AND F.FILM_ID = FD.FILM_ID AND FD.DIRECTOR_ID = ? ORDER BY F.RATE DESC";

        return jdbcTemplate.query(sql, FilmDbStorage::filmMapper, directorId);
    }

    private static Director directorMapper(ResultSet rs, int rowNum) throws SQLException {
        Director director = new Director();
        director.setId(rs.getLong("DIRECTOR_ID"));
        director.setName(rs.getString("DIRECTOR_NAME"));
        return director;
    }
}
