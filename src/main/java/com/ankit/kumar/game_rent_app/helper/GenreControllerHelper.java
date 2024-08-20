package com.ankit.kumar.game_rent_app.helper;

import com.ankit.kumar.game_rent_app.dao.GenreDao;
import com.ankit.kumar.game_rent_app.dao.model.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class GenreControllerHelper {
    @Autowired
    private GenreDao genreDao;

    public ResponseEntity<List<Genre>> getAllGenre() throws SQLException {
        List<Genre> genreList = genreDao.getAllGenres();
        return new ResponseEntity<>(genreList, HttpStatus.OK);
    }

    public ResponseEntity<Genre> getGenre(final String id) throws SQLException {
        Optional<Genre> genre = genreDao.getGenre(id);
        if (genre.isPresent()) {
            return new ResponseEntity<>(genre.get(), HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Genre> createGenre(final Genre genre) throws SQLException {
        String id = UUID.randomUUID().toString();
        genre.setId(id);
        genreDao.saveGenre(genre);
        return new ResponseEntity<>(genre, HttpStatus.OK);
    }
}
