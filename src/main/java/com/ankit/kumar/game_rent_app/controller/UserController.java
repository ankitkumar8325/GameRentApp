package com.ankit.kumar.game_rent_app.controller;

import com.ankit.kumar.game_rent_app.dao.model.User;
import com.ankit.kumar.game_rent_app.service.UserControllerService;
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
    private UserControllerService userControllerService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() throws SQLException {
        return userControllerService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id) throws SQLException {
        return userControllerService.getUser(id);
    }

    @PostMapping("/createUser")
    public ResponseEntity<User> createUser(@RequestBody @Valid User user) throws SQLException {
        return userControllerService.createUser(user);
    }

    // delete user, then have to delete the mappings for the user and games...
}
