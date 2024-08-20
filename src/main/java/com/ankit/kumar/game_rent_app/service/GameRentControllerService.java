package com.ankit.kumar.game_rent_app.service;

import com.ankit.kumar.game_rent_app.dao.*;
import com.ankit.kumar.game_rent_app.dao.model.Game;
import com.ankit.kumar.game_rent_app.dao.model.Genre;
import com.ankit.kumar.game_rent_app.dao.model.User;
import com.ankit.kumar.game_rent_app.model.RentGameRequest;
import com.ankit.kumar.game_rent_app.model.RentGameResponse;
import com.ankit.kumar.game_rent_app.model.RentedGameResponse;
import com.ankit.kumar.game_rent_app.model.RentedGameResponse.SinglGameDetail;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class GameRentControllerService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserGameRelationDao userGameRelationDao;

    @Autowired
    private GenreGameRelationDao genreGameRelationDao;

    @Autowired
    private GenreDao genreDao;

    @Autowired
    private GameDao gameDao;

    // We would want this to be configurable going forward
    private static final Integer MAX_ALLOWED_GAME_COUNT = 3;


    public ResponseEntity<RentedGameResponse> getAllGamesRentedByUser(@PathVariable String id) throws SQLException {

        Optional<User> user = userDao.getUser(id);
        if (user.isEmpty()) {
            return new ResponseEntity<>(RentedGameResponse.builder()
                    .message("Requested user does not exist in our database")
                    .build(), HttpStatus.OK);
        }

        // get all games for the user
        List<Game> gameList = userGameRelationDao.getAllGamesForUser(id);

        // for each game, get genre and fill genre details
        RentedGameResponse rentedGameResponse = getRentedGameResponse(gameList);
        return new ResponseEntity<>(rentedGameResponse, HttpStatus.OK);
    }


    public ResponseEntity<RentGameResponse> rentTheGame(@NonNull RentGameRequest rentGameRequest) throws SQLException {

        Optional<User> user = userDao.getUser(rentGameRequest.getUserId());
        if (user.isEmpty()) {
            return new ResponseEntity<>(RentGameResponse.builder()
                    .message("Requested user does not exist in our database")
                    .build(), HttpStatus.BAD_REQUEST);
        }

        // get all games for the user
        List<Game> gameList = userGameRelationDao.getAllGamesForUser(rentGameRequest.getUserId());
        if (gameList.size() >= MAX_ALLOWED_GAME_COUNT) {
            return new ResponseEntity<>(RentGameResponse.builder()
                    .message("User has already exceeded the quota!!")
                    .build(), HttpStatus.BAD_REQUEST);
        }

        // check if game exist or not
        Optional<Game> game = gameDao.getGame(rentGameRequest.getGameTitle(), rentGameRequest.getGameStudio());
        if (game.isEmpty()) {
            return new ResponseEntity<>(RentGameResponse.builder()
                    .message("Requested game is not present in the catalog!!")
                    .build(), HttpStatus.BAD_REQUEST);
        }

        // add entry in user-game relation
        userGameRelationDao.saveUserGameRelation(rentGameRequest.getUserId(), rentGameRequest.getGameTitle(), rentGameRequest.getGameStudio());
        return new ResponseEntity<>(RentGameResponse.builder()
                .message("User have been provided the rented game access!!")
                .build(), HttpStatus.OK);
    }

    public ResponseEntity<RentGameResponse> returnTheGame(@NonNull RentGameRequest rentGameRequest) throws SQLException {

        // validate that return game flag is set to true
        if(!Boolean.TRUE.equals(rentGameRequest.getReturnGameRequest())) {
            return new ResponseEntity<>(RentGameResponse.builder()
                    .message("Request does not have the 'returnGameRequest' flag as true")
                    .build(), HttpStatus.BAD_REQUEST);
        }

        Optional<User> user = userDao.getUser(rentGameRequest.getUserId());
        if (user.isEmpty()) {
            return new ResponseEntity<>(RentGameResponse.builder()
                    .message("Requested user does not exist in our database")
                    .build(), HttpStatus.BAD_REQUEST);
        }

        // get the games for the user and check if the game we are trying to return even exist or not
        Optional<Game> game = userGameRelationDao.getSingleGamesForUser(rentGameRequest.getUserId(), rentGameRequest.getGameTitle(), rentGameRequest.getGameStudio());
        if (game.isEmpty()) {
            return new ResponseEntity<>(RentGameResponse.builder()
                    .message("The game we are trying to return is not rented by the user!")
                    .build(), HttpStatus.BAD_REQUEST);
        }

        // delete entry from user-game relation
        userGameRelationDao.deleteUserGameRelation(rentGameRequest.getUserId(), rentGameRequest.getGameTitle(), rentGameRequest.getGameStudio());
        return new ResponseEntity<>(RentGameResponse.builder()
                .message("User has returned the game and therefore Quota is increased by one, You can rent a new game now. Yayy!!")
                .build(), HttpStatus.OK);
    }


    // helper methods form here onwards

    /**
     * This method gets the genre relation, fill genre information and etc.
     *
     * @param gameList
     * @return
     */
    private RentedGameResponse getRentedGameResponse(List<Game> gameList) throws SQLException {
        List<SinglGameDetail> singlGameDetailList = new ArrayList<>();
        for (Game game : gameList) {
            String gameTitle = game.getTitle();
            String gameStudio = game.getStudio();
            // get relation with genre
            List<String> genreIdList = genreGameRelationDao.getAllGenresForGame(gameTitle, gameStudio);

            // get and fill genre names
            List<String> genreNameList = new ArrayList<>();
            for (String genId : genreIdList) {
                Optional<Genre> gen = genreDao.getGenre(genId);
                gen.ifPresent(genre -> genreNameList.add(genre.getGenreName()));
            }

            // create singe game detail
            SinglGameDetail singlGameDetail = SinglGameDetail.builder()
                    .gameName(gameTitle)
                    .gameStudio(gameStudio)
                    .gameGenreList(genreNameList)
                    .build();

            singlGameDetailList.add(singlGameDetail);
        }

        return RentedGameResponse.builder()
                .gameDetailList(singlGameDetailList)
                .build();
    }

}
