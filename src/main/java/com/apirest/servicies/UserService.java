package com.apirest.servicies;

import com.apirest.exceptions.UserNotFoundException;
import com.apirest.exceptions.UserValidationException;
import com.apirest.models.UserEntity;
import com.apirest.repositories.UserRepository;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public UserEntity addUser(UserEntity userToAdd) {
        try {
            return userRepository.save(userToAdd);
        } catch (DataIntegrityViolationException e) {
            throw new UserValidationException("Email is already in use.", e);
        } catch (ConstraintViolationException e) {
            throw new UserValidationException("Validation error when creating user", e);
        }

    }

    @Transactional(readOnly = true)
    public UserEntity getUserById(Long searchedUserId) {
        if (searchedUserId == null) {
            throw new IllegalArgumentException("User ID cannot be null.");
        }
        return userRepository.findById(searchedUserId)
                .orElseThrow(() -> new  UserNotFoundException("User with ID: " + searchedUserId + " not found."));
    }

    @Transactional(readOnly = true)
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public UserEntity updateUserById(Long userIdToUpdate, UserEntity updatedUser) {
        UserEntity userToUpdate = userRepository.findById(userIdToUpdate)
                .orElse(null);
        userToUpdate.setFirstName(updatedUser.getFirstName());
        userToUpdate.setLastName(updatedUser.getLastName());
        userToUpdate.setEmail(updatedUser.getEmail());
        return userRepository.save(userToUpdate);
    }
}
