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
import java.util.Objects;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public UserEntity addUser(UserEntity userToAdd) {
        validateUser(userToAdd);
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
        Objects.requireNonNull(searchedUserId, "User ID cannot be null.");

        return userRepository.findById(searchedUserId)
                .orElseThrow(() -> new  UserNotFoundException("User with ID: " + searchedUserId + " not found."));
    }

    @Transactional(readOnly = true)
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public UserEntity updateUserById(Long userIdToUpdate, UserEntity updatedUser) {
        Objects.requireNonNull(userIdToUpdate, "User ID cannot be null.");

        UserEntity userToUpdate = getUserById(userIdToUpdate);
        validateUser(updatedUser);

        updateUserData(userToUpdate, updatedUser);
        return userRepository.save(userToUpdate);
    }


    @Transactional
    public void deleteUserById(Long userIdToDelete) {
        getUserById(userIdToDelete);

        userRepository.deleteById(userIdToDelete);
    }

    private void validateUser(UserEntity userToValidate) {
        Objects.requireNonNull(userToValidate, "The user cannot be null.");
        Objects.requireNonNull(userToValidate.getFirstName(), "The 'firstName' field cannot be null.");
        Objects.requireNonNull(userToValidate.getLastName(), "The 'lastName' field cannot be null.");
        Objects.requireNonNull(userToValidate.getEmail(), "The 'email' field cannot be null.");
    }

    private void updateUserData(UserEntity userToUpdate, UserEntity updatedUser) {
        userToUpdate.setFirstName(updatedUser.getFirstName());
        userToUpdate.setLastName(updatedUser.getLastName());
        userToUpdate.setEmail(updatedUser.getEmail());
    }
}
