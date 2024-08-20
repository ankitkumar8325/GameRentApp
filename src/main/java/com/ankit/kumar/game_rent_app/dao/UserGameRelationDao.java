package com.ankit.kumar.game_rent_app.dao;

import com.ankit.kumar.game_rent_app.dao.model.Game;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserGameRelationDao {

    @Autowired
    @Qualifier("myDataSource")
    private DataSource dataSource;

    private static final String TABLE_NAME = "USER_GAME_RELATION";

    /**
     * This method returns the all game IDs for the requested user
     *
     * @param userId
     * @return
     * @throws SQLException
     */
    public List<Game> getAllGamesForUser(String userId) throws SQLException {
        // create a query, get connection and execute the query
        String query = "Select * from " + TABLE_NAME + " where userId = ?";
        Connection con = getConnection();
        PreparedStatement stmt = con.prepareStatement(query);

        // set the request params
        stmt.setString(1, userId);

        // execute the query
        ResultSet rs = stmt.executeQuery();

        // form response and generate users response list
        final List<Game> gameList = new ArrayList<>();
        while (rs.next()) {
            gameList.add(convertResultItemToGame(rs));
        }
        return gameList;
    }

    /**
     * This method will put user-game relation mapping
     *
     * @param userId
     * @param gameTitle
     * @param gameStudio
     * @throws SQLException
     */
    public void saveUserGameRelation(@NonNull String userId, @NonNull String gameTitle, @NonNull String gameStudio) throws SQLException {
        System.out.println("Trying to save game relation in table");
        String query = "Insert into " + TABLE_NAME + " (userId, gameTitle, gameStudio) values (?, ?, ?)";
        Connection con = getConnection();
        PreparedStatement stmt = con.prepareStatement(query);

        // set the request params
        stmt.setString(1, userId);
        stmt.setString(2, gameTitle);
        stmt.setString(3, gameStudio);

        // execute the query (PS: I am ignoring the integer value equal to number of rows effected returned, as dont think its useful)
        int r = stmt.executeUpdate();
        System.out.println("Row impacted: " + r);
    }

    // helper methods from here onwards
    private Game convertResultItemToGame(ResultSet rs) throws SQLException {
        return Game.builder()
                .title(rs.getString("gameTitle"))
                .studio(rs.getString("gameStudio"))
                .build();
    }

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }


}