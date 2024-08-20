package com.ankit.kumar.game_rent_app.helper;

import com.ankit.kumar.game_rent_app.dao.GameDao;
import com.ankit.kumar.game_rent_app.dao.GenreDao;
import com.ankit.kumar.game_rent_app.dao.GenreGameRelationDao;
import com.ankit.kumar.game_rent_app.dao.model.Game;
import com.ankit.kumar.game_rent_app.dao.model.Genre;
import com.ankit.kumar.game_rent_app.model.CreateGame;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class GameControllerHelper {

    @Autowired
    private GameDao gameDao;

    @Autowired
    private GenreDao genreDao;

    @Autowired
    private GenreGameRelationDao genreGameRelationDao;

    public ResponseEntity<List<Game>> getGames() throws SQLException {
        List<Game> gamesList = gameDao.getAllGames();
        return new ResponseEntity<>(gamesList, HttpStatus.OK);
    }

    public ResponseEntity<Game> getGame(@NonNull String title, @NonNull String studio) throws SQLException {
        Optional<Game> game = gameDao.getGame(title, studio);
        if (game.isPresent()) {
            return new ResponseEntity<>(game.get(), HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<CreateGame> createGame(@NonNull CreateGame createGame) throws SQLException {
        // Step 1. first check if genre exist, if not, create the genres and fetch genre ids
        List<String> allGenreIds = checkAndCreateGenre(createGame.getGenres());

        // Step 2. create entry in games table
        Game game = new Game(createGame.getGameTitle(), createGame.getGameStudio());
        gameDao.saveGame(game);

        // Step 3. create entry in genre game relation table
        for (String genreId : allGenreIds) {
            genreGameRelationDao.saveGenreGameRelation(genreId, createGame.getGameTitle(), createGame.getGameStudio());
        }

        return new ResponseEntity<>(createGame, HttpStatus.OK);
    }

    // helper methods here
    private List<String> checkAndCreateGenre(@NonNull List<String> genreNames) throws SQLException {
        List<String> genreIds = new ArrayList<>();
        for (String genreName : genreNames) {
            Optional<Genre> optionalGenre = genreDao.getGenreByName(genreName);
            if (optionalGenre.isEmpty()) {
                String id = UUID.randomUUID().toString();
                Genre genre = new Genre(id, genreName, "Default Description for Genre");
                genreDao.saveGenre(genre);
                genreIds.add(genre.getId());
            } else {
                genreIds.add(optionalGenre.get().getId());
            }
        }
        return genreIds;
    }

}
