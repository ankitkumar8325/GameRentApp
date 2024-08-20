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
import java.util.Optional;

/**
 * This is DAO class for GAMES table, Also assumption is that game is digitally rented, so same game can be rented to multiple people without any count or limit
 */
@Component
public class GameDao {

    @Autowired
    @Qualifier("myDataSource")
    private DataSource dataSource;

    private static final String TABLE_NAME = "GAMES";

    /**
     * This method will query the DB and return all the games in our DB.
     * Ideally we should paginate the response, but for sake of simplicity for demo, assuming DB is not too big
     *
     * @return
     * @throws SQLException
     */
    public List<Game> getAllGames() throws SQLException {
        // create a query, get connection and execute the query
        String query = "Select * from " + TABLE_NAME;
        Connection con = getConnection();
        PreparedStatement stmt = con.prepareStatement(query);

        try {
            // execute the query
            ResultSet rs = stmt.executeQuery();

            // form response and generate users response list
            final List<Game> gameList = new ArrayList<>();
            while (rs.next()) {
                gameList.add(convertResultItemToGame(rs));
            }
            return gameList;
        } catch (Exception e) {
            throw e;
        } finally {
            con.close();
        }
    }

    /**
     * This method will return the single Game entry queried using the primary key,
     * will return Optional.Empty if not present
     *
     * @param title
     * @param studio
     * @return
     * @throws SQLException
     */
    public Optional<Game> getGame(@NonNull String title, @NonNull String studio) throws SQLException {
        String query = "Select * from " + TABLE_NAME + " where title = ? and studio = ?";
        Connection con = getConnection();
        PreparedStatement stmt = con.prepareStatement(query);

        try {
            // set the request params
            stmt.setString(1, title);
            stmt.setString(2, studio);

            // execute the query
            ResultSet rs = stmt.executeQuery();

            // form the response using resultSet
            Optional<Game> game = Optional.empty();
            if (rs.next()) {
                game = Optional.of(convertResultItemToGame(rs));
            }
            return game;
        } catch (Exception e) {
            throw  e;
        } finally {
            con.close();
        }
    }

    /**
     * This method will be used to create a new game entry
     *
     * @param game
     * @throws SQLException
     */
    public void saveGame(@NonNull Game game) throws SQLException {
        String query = "Insert into " + TABLE_NAME + " (title, studio) values (?, ?)";
        Connection con = getConnection();
        PreparedStatement stmt = con.prepareStatement(query);

        try {
            // set the request params
            stmt.setString(1, game.getTitle());
            stmt.setString(2, game.getStudio());

            // execute the query
            int r = stmt.executeUpdate();
            System.out.println("Rows updated: " + r);
        } catch (Exception e) {
            throw e;
        } finally {
            con.close();
        }
    }


    // helper methods below this line
    private Game convertResultItemToGame(ResultSet rs) throws SQLException {
        return Game.builder()
                .title(rs.getString("title"))
                .studio(rs.getString("studio"))
                .build();
    }

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
