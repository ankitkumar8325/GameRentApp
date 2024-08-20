package com.ankit.kumar.game_rent_app.helper;

import com.ankit.kumar.game_rent_app.dao.UserDao;
import com.ankit.kumar.game_rent_app.dao.model.User;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserControllerHelper {

    @Autowired
    private UserDao userDao;

    public ResponseEntity<List<User>> getAllUsers() throws SQLException {
        List<User> userList = userDao.getAllUsers();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    public ResponseEntity<User> getUser(@NonNull String id) throws SQLException {
        Optional<User> user = userDao.getUser(id);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<User> createUser(@NonNull User user) throws SQLException {
        String id = UUID.randomUUID().toString();
        user.setId(id);
        userDao.saveUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
