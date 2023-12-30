package com.apirest.controllers;

import com.apirest.models.UserEntity;
import com.apirest.servicies.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
