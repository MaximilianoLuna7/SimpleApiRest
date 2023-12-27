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
        return userRepository.findById(searchedUserId)
                .orElseThrow(() -> new  UserNotFoundException("User with ID: " + searchedUserId + " not found."));
    }
}
