package com.ankit.kumar.game_rent_app.web;

import com.ankit.kumar.game_rent_app.dao.GameDao;
import com.ankit.kumar.game_rent_app.dao.model.Game;
import com.ankit.kumar.game_rent_app.helper.GameControllerHelper;
import com.ankit.kumar.game_rent_app.model.CreateGame;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.*;

@RestController
public class GameController {

    @Autowired
    private GameDao gameDao;

    @Autowired
    private GameControllerHelper gameControllerHelper;

    @GetMapping("/games")
    public ResponseEntity<List<Game>> getGames() throws SQLException {
        return gameControllerHelper.getGames();
    }

    @GetMapping("/games/{id}")
    public ResponseEntity<Game> getGame(@PathVariable String title, @PathVariable String studio) throws SQLException {
        return gameControllerHelper.getGame(title, studio);
    }

    @PostMapping("/createGame")
    public ResponseEntity<CreateGame> createGame(@RequestBody @Valid CreateGame createGame) throws SQLException {
        System.out.println("Request received: " + createGame);
        return gameControllerHelper.createGame(createGame);
    }


    // can not allow deletion of game
    // as it might be rented by some user when we are trying to delete its entry
}
