package com.apirest.servicies;

import com.apirest.exceptions.UserNotFoundException;
import com.apirest.models.UserEntity;
import com.apirest.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Create new user - Successful")
    void createUser_Successful() {
        // Arrange
        UserEntity userToAdd = createUser();

        when(userRepository.save(userToAdd)).thenReturn(userToAdd);

        // Act
        userService.addUser(userToAdd);

        // Assert
        verify(userRepository, times(1)).save(userToAdd);
    }

    @Test
    @DisplayName("Get user - Successful")
    void getUser_Successful() {
        // Arrange
        UserEntity userToAdd = createUser();
        Long userId = userToAdd.getId();

        when(userRepository.findById(userId)).thenReturn(Optional.of(userToAdd));

        // Act
        UserEntity user = userService.getUserById(userId);

        // Assert
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(userId);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Get user with non existent user id - Throws exception")
    void getUserWithNonExistentId_ThrowsException() {
        // Arrange
        Long nonExistentId = 999L;

        when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userService.getUserById(nonExistentId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User with ID: " + nonExistentId + " not found.");

        verify(userRepository, times(1)).findById(nonExistentId);
    }

    @Test
    @DisplayName("Get user with invalid user id - Throws exception")
    void getUserWithInvalidId_ThrowsException() {
        // Arrange
        Long invalidId = 0L;

        // Act & Assert
        assertThatThrownBy(() -> userService.getUserById(invalidId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid user ID: " + invalidId);

        verifyNoInteractions(userRepository);
    }

    @Test
    @DisplayName("Get all users - Successful")
    void getAllUsers_Successful() {
        // Arrange
        List<UserEntity> expectedUsers = new ArrayList<>();

        UserEntity user1 = createUser();
        UserEntity user2 = UserEntity.builder()
                .id(2L)
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .build();

        expectedUsers.add(user1);
        expectedUsers.add(user2);

        when(userRepository.findAll()).thenReturn(expectedUsers);

        // Act
        List<UserEntity> actualUsers = userService.getAllUsers();

        // Assert
        assertThat(actualUsers).isNotNull();
        assertThat(actualUsers)
                .hasSize(expectedUsers.size())
                .containsAll(expectedUsers);

        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Delete user - Successful")
    void deleteUser_Successful() {
        // Arrange
        UserEntity user = createUser();
        Long userId = user.getId();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        userService.deleteUser(userId);

        // Assert
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    @DisplayName("Delete user with non existent id - Throws exception")
    void deleteUserWithNonExistentId_ThrowsException() {
        // Arrange
        Long nonExistentId = 999L;

        when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userService.deleteUser(nonExistentId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User with ID: " + nonExistentId + " not found.");

        verify(userRepository, times(1)).findById(nonExistentId);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Delete user with invalid id - Throws exception")
    void deleteUserWithInvalidId_ThrowsException() {
        // Arrange
        Long invalidId = 0L;

        // Act & Assert
        assertThatThrownBy(() -> userService.deleteUser(invalidId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid user ID: " + invalidId);

        verifyNoInteractions(userRepository);
    }

    private UserEntity createUser() {
        return UserEntity.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();
    }
}
