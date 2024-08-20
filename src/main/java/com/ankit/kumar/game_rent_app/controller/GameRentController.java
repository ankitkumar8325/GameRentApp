package com.ankit.kumar.game_rent_app.controller;

import com.ankit.kumar.game_rent_app.service.GameRentControllerService;
import com.ankit.kumar.game_rent_app.model.RentGameRequest;
import com.ankit.kumar.game_rent_app.model.RentGameResponse;
import com.ankit.kumar.game_rent_app.model.RentedGameResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
public class GameRentController {

    @Autowired
    private GameRentControllerService gameRentControllerService;

    @GetMapping("/")
    public ResponseEntity<String> hello() {
        return new ResponseEntity<>("Welcome to Game Renting Portal", HttpStatus.OK);
    }

    @GetMapping("/gameRentedByUser/{id}")
    public ResponseEntity<RentedGameResponse> getAllGamesRentedByUser(@PathVariable String id) throws SQLException {
        return gameRentControllerService.getAllGamesRentedByUser(id);
    }

    @PostMapping("rentGame")
    public ResponseEntity<RentGameResponse> rentTheGame(@RequestBody @Valid RentGameRequest rentGameRequest) throws SQLException {
        return gameRentControllerService.rentTheGame(rentGameRequest);
    }

    @DeleteMapping("returnGame")
    public ResponseEntity<RentGameResponse> returnTheGame(@RequestBody @Valid RentGameRequest rentGameRequest) throws SQLException {
        return gameRentControllerService.returnTheGame(rentGameRequest);
    }

}
