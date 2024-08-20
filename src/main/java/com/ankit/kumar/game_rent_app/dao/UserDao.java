package com.ankit.kumar.game_rent_app.dao;

import com.ankit.kumar.game_rent_app.dao.model.User;
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
 * This is DAO class for USERS table
 */
@Component
public class UserDao {
    @Autowired
    @Qualifier("myDataSource")
    private DataSource dataSource;

    private static final String TABLE_NAME = "USERS";

    /**
     * This method will query the DB and return all the users in our DB.
     * Ideally we should paginate the response, but for sake of simplicity for demo, assuming DB is not too big
     *
     * @return
     * @throws SQLException
     */
    public List<User> getAllUsers() throws SQLException {
        // create a query, get connection and execute the query
        String query = "Select * from " + TABLE_NAME;
        Connection con = getConnection();
        PreparedStatement stmt = con.prepareStatement(query);

        // execute the query
        ResultSet rs = stmt.executeQuery();

        // form response and generate users response list
        final List<User> userList = new ArrayList<>();
        while (rs.next()) {
            userList.add(convertResultItemToUser(rs));
        }
        return userList;
    }

    /**
     * This method will return the User entry queried using the primary key,
     * will return Optional.Empty if not present
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public Optional<User> getUser(@NonNull String id) throws SQLException {
        String query = "Select * from " + TABLE_NAME + " where id = ?";
        Connection con = getConnection();
        PreparedStatement stmt = con.prepareStatement(query);

        // set the request params
        stmt.setString(1, id);

        // execute the query
        ResultSet rs = stmt.executeQuery();

        // form the response using resultSet
        Optional<User> user = Optional.empty();
        if (rs.next()) {
            user = Optional.of(convertResultItemToUser(rs));
        }
        return user;

    }


    /**
     * This method will be used to create a new user entry
     *
     * @param user
     * @throws SQLException
     */
    public void saveUser(@NonNull User user) throws SQLException {
        String query = "Insert into " + TABLE_NAME + " (id, username, name) values (?, ?, ?)";
        Connection con = getConnection();
        PreparedStatement stmt = con.prepareStatement(query);

        // change username to uppercase, so that different case username are not considered differemt
        user.setUsername(user.getUsername().toUpperCase());

        // set the request params
        stmt.setString(1, user.getId().toString());
        stmt.setString(2, user.getUsername());
        stmt.setString(3, user.getName());

        // execute the query
        int r = stmt.executeUpdate();
        System.out.println("Rows updated: " + r);
    }


    // helper methods below this line
    private User convertResultItemToUser(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getString("id"))
                .username(rs.getString("username"))
                .name(rs.getString("name"))
                .build();
    }

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
