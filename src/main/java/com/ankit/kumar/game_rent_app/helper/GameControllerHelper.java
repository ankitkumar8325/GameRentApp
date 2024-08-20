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
        System.out.println("Returning now the games response using entity response");
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
        System.out.println("Request received: " + createGame);

        // Step 1. first check if genre exist, if not, create the genres and fetch genre ids
        List<String> allGenreIds = checkAndCreateGenre(createGame.getGenres());

        // Step 2. create entry in games table
        System.out.println("Creating game entry");
        Game game = new Game(createGame.getGameTitle(), createGame.getGameStudio());
        gameDao.saveGame(game);
        System.out.println("Game entry created");

        // Step 3. create entry in genre game relation table
        System.out.println("All genre Ids list: " + allGenreIds);
        System.out.println("Strting to update game-genre relation");
        for (String genreId : allGenreIds) {
            System.out.println("Updating for gen-id: " + genreId);
            genreGameRelationDao.saveGenreGameRelation(genreId, createGame.getGameTitle(), createGame.getGameStudio());
            System.out.println("Final Update Done");
        }
        System.out.println("Update done for all");

        return new ResponseEntity<>(createGame, HttpStatus.OK);
    }

    // helper methods here
    private List<String> checkAndCreateGenre(@NonNull List<String> genreNames) throws SQLException {
        List<String> genreIds = new ArrayList<>();
        for (String genreName : genreNames) {
            System.out.println("Checking for genre: " + genreName);
            Optional<Genre> optionalGenre = genreDao.getGenreByName(genreName);
            if (optionalGenre.isEmpty()) {
                System.out.println("Genre already exist");
                String id = UUID.randomUUID().toString();
                Genre genre = new Genre(id, genreName, "Default Description for Genre");
                genreDao.saveGenre(genre);
                genreIds.add(genre.getId());
            } else {
                System.out.println("Genre not exist");
                genreIds.add(optionalGenre.get().getId());
                System.out.println("Genre created");
            }
        }
        return genreIds;
    }

}
