package com.ankit.kumar.game_rent_app.controller;

import com.ankit.kumar.game_rent_app.dao.model.Genre;
import com.ankit.kumar.game_rent_app.service.GenreControllerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

/**
 * This controller hold all the rest endpoints for modification of user
 */
@RestController
public class GenreController {

    @Autowired
    private GenreControllerService genreControllerService;

    @GetMapping("/genres")
    public ResponseEntity<List<Genre>> getAllGenres() throws SQLException {
        return genreControllerService.getAllGenre();
    }


    @GetMapping("/genres/{id}")
    public ResponseEntity<Genre> getGenre(@PathVariable String id) throws SQLException {
        return genreControllerService.getGenre(id);
    }

    @PostMapping("/createGenre")
    public ResponseEntity<Genre> createGenre(@RequestBody @Valid Genre genre) throws SQLException {
        return genreControllerService.createGenre(genre);
    }

    // Delete genre is not added because, if we delete genre we may have to remove it from the games that already have it
    // might make implementation complex
}
