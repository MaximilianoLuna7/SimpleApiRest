package com.apirest.servicies;

import com.apirest.models.UserEntity;
import com.apirest.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserEntity addUser(UserEntity userToAdd) {
        return userRepository.save(userToAdd);
    }
}
