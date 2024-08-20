package com.ankit.kumar.game_rent_app.web;

import com.ankit.kumar.game_rent_app.dao.model.User;
import com.ankit.kumar.game_rent_app.helper.UserControllerHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

/**
 * This controller hold all the rest endpoints for modification of user
 */
@RestController
public class UserController {


    @Autowired
    private UserControllerHelper userControllerHelper;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() throws SQLException {
        return userControllerHelper.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id) throws SQLException {
        return userControllerHelper.getUser(id);
    }

    @PostMapping("/createUser")
    public ResponseEntity<User> createUser(@RequestBody @Valid User user) throws SQLException {
        return userControllerHelper.createUser(user);
    }

    // delete user, then have to delete the mappings for the user and games...
}
