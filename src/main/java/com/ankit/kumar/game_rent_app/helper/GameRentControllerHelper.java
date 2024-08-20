package com.ankit.kumar.game_rent_app.helper;

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
public class GameRentControllerHelper {

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
        System.out.println("Valid user");

        // get all games for the user
        List<Game> gameList = userGameRelationDao.getAllGamesForUser(rentGameRequest.getUserId());
        System.out.println("Game list: " + gameList);
        if (gameList.size() >= MAX_ALLOWED_GAME_COUNT) {
            return new ResponseEntity<>(RentGameResponse.builder()
                    .message("User has already exceeded the quota!!")
                    .build(), HttpStatus.BAD_REQUEST);
        }
        System.out.println("user limit not exhausted");

        // check if game exist or not
        Optional<Game> game = gameDao.getGame(rentGameRequest.getGameTitle(), rentGameRequest.getGameStudio());
        if (game.isEmpty()) {
            return new ResponseEntity<>(RentGameResponse.builder()
                    .message("Requested game is not present in the catalog!!")
                    .build(), HttpStatus.BAD_REQUEST);
        }
        System.out.println("Game exist in DB");

        // add entry in user-game relation
        userGameRelationDao.saveUserGameRelation(rentGameRequest.getUserId(), rentGameRequest.getGameTitle(), rentGameRequest.getGameStudio());
        System.out.println("Updated game relation");
        return new ResponseEntity<>(RentGameResponse.builder()
                .message("User have been provided the rented game access!!")
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
