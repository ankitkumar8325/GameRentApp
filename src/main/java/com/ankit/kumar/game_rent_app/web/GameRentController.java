package com.ankit.kumar.game_rent_app.web;

import com.ankit.kumar.game_rent_app.helper.GameRentControllerHelper;
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
    private GameRentControllerHelper gameRentControllerHelper;

    @GetMapping("/")
    public ResponseEntity<String> hello() {
        return new ResponseEntity<>("Welcome to Game Renting Portal", HttpStatus.OK);
    }

    @GetMapping("/gameRentedByUser/{id}")
    public ResponseEntity<RentedGameResponse> getAllGamesRentedByUser(@PathVariable String id) throws SQLException {
        return gameRentControllerHelper.getAllGamesRentedByUser(id);
    }

    @PostMapping("rentGame")
    public ResponseEntity<RentGameResponse> rentTheGame(@RequestBody @Valid RentGameRequest rentGameRequest) throws SQLException {
        return gameRentControllerHelper.rentTheGame(rentGameRequest);
    }

}
