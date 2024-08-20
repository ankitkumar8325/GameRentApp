package com.ankit.kumar.game_rent_app.dao;

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
public class GenreGameRelationDao {
    @Autowired
    @Qualifier("myDataSource")
    private DataSource dataSource;

    private static final String TABLE_NAME = "GENRES_GAME_RELATION";

    /**
     * This method returns the all genreIds for the asked game
     *
     * @param gameTitle
     * @param gameStudio
     * @return
     * @throws SQLException
     */
    public List<String> getAllGenresForGame(@NonNull String gameTitle, @NonNull String gameStudio) throws SQLException {
        // create a query, get connection and execute the query
        String query = "Select * from " + TABLE_NAME + " where gameTitle = ? and gameStudio = ?";
        Connection con = getConnection();
        PreparedStatement stmt = con.prepareStatement(query);

        try {
            // set the request params
            stmt.setString(1, gameTitle);
            stmt.setString(2, gameStudio);

            // execute the query
            ResultSet rs = stmt.executeQuery();

            // form response and generate users response list
            final List<String> genreIdList = new ArrayList<>();
            while (rs.next()) {
                genreIdList.add(rs.getString("genreId"));
            }
            return genreIdList;
        } catch (Exception e) {
            throw e;
        } finally {
            con.close();
        }
    }

    /**
     * This method will put game genre relation mapping
     *
     * @param genreId
     * @param gameTitle
     * @param gameStudio
     * @throws SQLException
     */
    public void saveGenreGameRelation(@NonNull String genreId, @NonNull String gameTitle, @NonNull String gameStudio) throws SQLException {
        String query = "Insert into " + TABLE_NAME + " (genreId, gameTitle, gameStudio) values (?, ?, ?)";
        Connection con = getConnection();
        PreparedStatement stmt = con.prepareStatement(query);

        try {
            // set the request params
            stmt.setString(1, genreId);
            stmt.setString(2, gameTitle);
            stmt.setString(3, gameStudio);

            // execute the query
            int r = stmt.executeUpdate();
            System.out.println("Rows updated: " + r);
        } catch (Exception e) {
            throw e;
        } finally {
            con.close();
        }
    }

    // helper methods from here onwards
    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
