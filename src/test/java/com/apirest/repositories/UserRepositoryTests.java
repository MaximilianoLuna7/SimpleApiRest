package com.apirest.repositories;

import com.apirest.models.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DataJpaTest
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Save user - Successful")
    void saveUser_Successful() {
        // Arrange
        UserEntity user = createUser();

        // Act
        UserEntity savedUser = userRepository.save(user);

        // Assert
        assertThat(savedUser.getId()).isNotNull();
    }

    @Test
    @DisplayName("Find user by id - Successful")
    void findUserById_Successful() {
        // Arrange
        UserEntity user = createUser();
        UserEntity savedUser = userRepository.save(user);

        // Act
        Optional<UserEntity> foundUserOptional = userRepository.findById(savedUser.getId());

        // Assert
        assertThat(foundUserOptional).isPresent();
        assertThat(foundUserOptional.get().getId()).isEqualTo(savedUser.getId());
    }

    @Test
    @DisplayName("Find user by id - Not found")
    void findUserById_NotFound() {
        // Arrange
        Long nonExistentId = 999L;

        // Act
        Optional<UserEntity> foundUserOptional = userRepository.findById(nonExistentId);

        // Assert
        assertThat(foundUserOptional).isEmpty();
    }

    @Test
    @DisplayName("Find all users - Successful")
    void findAllUsers_Successful() {
        // Arrange
        UserEntity user1 = createUser();
        UserEntity user2 = UserEntity.builder()
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        // Act
        List<UserEntity> foundUsers = userRepository.findAll();

        // Assert
        assertThat(foundUsers)
                .isNotNull()
                .hasSize(2);
        assertThat(foundUsers).contains(user1, user2);
    }

    @Test
    @DisplayName("Delete user - Successful")
    void deleteUser_Successful() {
        // Arrange
        UserEntity user = createUser();
        UserEntity savedUser = userRepository.save(user);

        // Act
        userRepository.delete(user);

        // Assert
        Optional<UserEntity> deletedUserOptional = userRepository.findById(savedUser.getId());
        assertThat(deletedUserOptional).isEmpty();
    }

    private UserEntity createUser() {
        return UserEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();
    }
}
