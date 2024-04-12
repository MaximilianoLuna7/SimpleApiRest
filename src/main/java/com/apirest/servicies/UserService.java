package com.apirest.servicies;

import com.apirest.exceptions.DataBaseErrorException;
import com.apirest.exceptions.UserNotFoundException;
import com.apirest.models.UserEntity;
import com.apirest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void addUser(UserEntity userToAdd) {
        try {
            userRepository.save(userToAdd);
        } catch (DataAccessException ex) {
            throw new DataBaseErrorException("Error creating user: " + ex.getMessage());
        }
    }

    public UserEntity getUserById(Long userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID: " + userId);
        }

        return userRepository.findById(userId)
                .orElseThrow(() -> new  UserNotFoundException("User with ID: " + userId + " not found."));
    }

    public List<UserEntity> getAllUsers() {
        try {
            return userRepository.findAll();
        } catch (DataAccessException ex) {
            throw new DataBaseErrorException("Error listing users: " + ex.getMessage());
        }
    }

    @Transactional
    public void updateUserById(Long userIdToUpdate, UserEntity updatedUser) {
        if (userIdToUpdate == null || userIdToUpdate <= 0) {
            throw new IllegalArgumentException("Invalid user ID: " + userIdToUpdate);
        }

        try {
            UserEntity userToUpdate = getUserById(userIdToUpdate);
            updateUserData(userToUpdate, updatedUser);
            userRepository.save(userToUpdate);
        } catch (DataAccessException ex) {
            throw new DataBaseErrorException("Error updating user: " + ex.getMessage());
        }
    }

    @Transactional
    public void deleteUser(Long userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID: " + userId);
        }

        try {
            UserEntity userToDelete = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User with ID: " + userId + " not found."));

            userRepository.delete(userToDelete);
        } catch (DataAccessException ex) {
            throw new DataBaseErrorException("Error deleting user: " + ex.getMessage());
        }
    }

    private void updateUserData(UserEntity userToUpdate, UserEntity updatedUser) {
        userToUpdate.setFirstName(updatedUser.getFirstName());
        userToUpdate.setLastName(updatedUser.getLastName());
        userToUpdate.setEmail(updatedUser.getEmail());
    }
}
