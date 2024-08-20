package com.ankit.kumar.game_rent_app.dao;

import com.ankit.kumar.game_rent_app.dao.model.Genre;
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
 * This is DAO class for GENRES table
 */
@Component
public class GenreDao {

    @Autowired
    @Qualifier("myDataSource")
    private DataSource dataSource;

    private static final String TABLE_NAME = "GENRES";

    /**
     * This method will query the DB and return all the genres in our DB.
     * Ideally we should paginate the response, but for sake of simplicity for demo, assuming DB is not too big
     *
     * @return
     * @throws SQLException
     */
    public List<Genre> getAllGenres() throws SQLException {
        // create a query, get connection and execute the query
        String query = "Select * from " + TABLE_NAME;
        Connection con = getConnection();
        PreparedStatement stmt = con.prepareStatement(query);

        try {
            // execute the query
            ResultSet rs = stmt.executeQuery();

            // form response and generate users response list
            final List<Genre> genreList = new ArrayList<>();
            while (rs.next()) {
                genreList.add(convertResultItemToGenre(rs));
            }
            return genreList;
        } catch (Exception e) {
            throw e;
        } finally {
            con.close();
        }
    }

    /**
     * This method will return the single Genre entry queried using the primary key,
     * will return Optional.Empty if not present
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public Optional<Genre> getGenre(@NonNull String id) throws SQLException {
        String query = "Select * from " + TABLE_NAME + " where id = ?";
        Connection con = getConnection();
        PreparedStatement stmt = con.prepareStatement(query);

        try {
            // set the request params
            stmt.setString(1, id);

            // execute the query
            ResultSet rs = stmt.executeQuery();

            // form the response using resultSet
            Optional<Genre> genre = Optional.empty();
            if (rs.next()) {
                genre = Optional.of(convertResultItemToGenre(rs));
            }
            return genre;
        } catch (Exception e) {
            throw e;
        } finally {
            con.close();
        }
    }

    /**
     * This method will return the single Genre entry queried using the genre name,
     * will return Optional.Empty if not present
     *
     * @param genreName
     * @return
     * @throws SQLException
     */
    public Optional<Genre> getGenreByName(@NonNull String genreName) throws SQLException {
        String query = "Select * from " + TABLE_NAME + " where genreName = ?";
        Connection con = getConnection();
        PreparedStatement stmt = con.prepareStatement(query);

        try {

            // update the genre to uppercase
            genreName = genreName.toUpperCase();

            // set the request params
            stmt.setString(1, genreName);

            // execute the query
            ResultSet rs = stmt.executeQuery();

            // form the response using resultSet
            Optional<Genre> genre = Optional.empty();
            if (rs.next()) {
                genre = Optional.of(convertResultItemToGenre(rs));
            }
            return genre;
        } catch (Exception e) {
            throw e;
        } finally {
            con.close();
        }
    }

    /**
     * This method will be used to create a new genre entry
     *
     * @param genre
     * @throws SQLException
     */
    public void saveGenre(@NonNull Genre genre) throws SQLException {
        String query = "Insert into " + TABLE_NAME + " (id, genreName, genreDetail) values (?, ?, ?)";
        Connection con = getConnection();
        PreparedStatement stmt = con.prepareStatement(query);

        try {
            // change genre name to UPPER CASE, so it's not duplicated if someone provide different case
            genre.setGenreName(genre.getGenreName().toUpperCase());

            // set the request params
            stmt.setString(1, genre.getId());
            stmt.setString(2, genre.getGenreName());
            stmt.setString(3, genre.getGenreDetail());

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
    private Genre convertResultItemToGenre(ResultSet rs) throws SQLException {
        return Genre.builder()
                .id(rs.getString("id"))
                .genreName(rs.getString("genreName"))
                .genreDetail(rs.getString("genreDetail"))
                .build();
    }

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

}
