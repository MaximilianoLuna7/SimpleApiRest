package com.apirest.controllers;

import com.apirest.models.UserEntity;
import com.apirest.servicies.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity userToCreate) {
        UserEntity createdUser = userService.addUser(userToCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable Long userId) {
        UserEntity userToGet = userService.getUserById(userId);
        return ResponseEntity.ok(userToGet);
    }

    @GetMapping
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        List<UserEntity> usersList = userService.getAllUsers();
        return ResponseEntity.ok(usersList);
    }
}
