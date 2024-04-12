package com.apirest.controllers;

import com.apirest.models.UserEntity;
import com.apirest.servicies.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody UserEntity userToCreate) {
        userService.addUser(userToCreate);
        return ResponseEntity.ok("User created successfully");
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

    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable Long userId, @RequestBody UserEntity updatedUser) {
        userService.updateUserById(userId, updatedUser);
        return ResponseEntity.ok("User updated successfully");
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String > deleteUserById(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully");
    }
}
