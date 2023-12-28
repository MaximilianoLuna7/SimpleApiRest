package com.apirest;

import com.apirest.exceptions.UserNotFoundException;
import com.apirest.exceptions.UserValidationException;
import com.apirest.models.UserEntity;
import com.apirest.repositories.UserRepository;
import com.apirest.servicies.UserService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void addUserShouldAddANewUser() {
        //Given a user to add with correct values in their fields
        UserEntity userToAdd = UserEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        // When the user is added, simulate the save operation in the repository
        when(userRepository.save(userToAdd)).thenReturn(userToAdd);

        // Perform the addUser operation
        UserEntity AddedUser = userService.addUser(userToAdd);

        // Then assert that the added user is equal to the user provider
        assertThat(AddedUser).isEqualTo(userToAdd);
        // Verify that the save operation in the repository was called exactly once
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void addUserWithExistingEmailShouldThrowException() {
        // Given an existing user in the database
        UserEntity existingUser = UserEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        // Simulate that the repository will return an existing user with the same email
        when(userRepository.save(existingUser)).thenThrow(DataIntegrityViolationException.class);

        // When attempting to add a new user with the same email should throw an instance of the UserValidationException class
        assertThatThrownBy(() -> userService.addUser(existingUser))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("Email is already in use");

        // Ensure that the save method was called exactly once with the provided user
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void addUserWithInvalidFieldsShouldThrowException() {
        // Given a user with an empty field
        UserEntity userWithInvalidFields = UserEntity.builder()
                .firstName("")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        // Simulate that the repository will throw a ConstraintViolationException when trying to save
        when(userRepository.save(userWithInvalidFields)).thenThrow(ConstraintViolationException.class);

        //When attempting to add a user with an empty field should throw a UserValidationException
        assertThatThrownBy(() -> userService.addUser(userWithInvalidFields))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("Validation error when creating user");

        // Ensure that the save method was called exactly once wih the provided user
        verify(userRepository, times(1)).save(userWithInvalidFields);
    }

    @Test
    void getUserByIdShouldReturnCorrectUser() {
        // Given an Id and user to search
        Long searchedUserId = 1L;
        UserEntity searchedUser = UserEntity.builder()
                .id(searchedUserId)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        // Simulate that the findById method in the repository return an optional of searchedUser when find id user
        when(userRepository.findById(searchedUserId)).thenReturn(Optional.of(searchedUser));

        // Perform the findUserById operation
        UserEntity userFound = userService.getUserById(searchedUserId);

        // Assert that the found user is equal to the searched user
        assertThat(userFound).isEqualTo(searchedUser);
    }

    @Test
    void getUserByIdWithNonExistentIdThrowsException() {
        // Given a non-existent user id
        Long nonExistentUserId = 999L;

        // Simulate that the findById method in the repository returns an empty optional of UserEntity when user id is not found
        when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        // When attempting to get a user with non-existent id should throw a UserNotFoundException
        assertThatThrownBy(() -> userService.getUserById(nonExistentUserId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User with ID: " + nonExistentUserId + " not found");

        // Ensure that the repository.findBiId method was called exactly once with the provided id
        verify(userRepository, times(1)).findById(nonExistentUserId);
    }

    @Test
    void getUserByNullIdShouldThrowsException() {
        // Given a null user id
        Long nullUserId = null;

        // When attempting to get a user with null id should throw a UserNotFoundException
        assertThatThrownBy(() -> userService.getUserById(nullUserId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User ID cannot be null");

        // Ensure that the repository.findById method was not called
        verify(userRepository, never()).findById(any());
    }

    @Test
    void getAllUsersShouldReturnListOfAllUsers() {
        // Given a list of users
        List<UserEntity> usersList = Arrays.asList(
            new UserEntity(1L, "John", "Doe", "john.doe@example.com"),
            new UserEntity(2L, "Mike", "Smith", "mike.smith@example.com"),
            new UserEntity(3L, "Sara", "Jones", "sara.jones@example.com")
        );

        // Simulate that findAll method in the repository returns a list of users
        when(userRepository.findAll()).thenReturn(usersList);

        // Perform the getAllUsers operation
        List<UserEntity> allUsersList = userService.getAllUsers();

        // Assert that the list of users obtained is correct
        assertThat(allUsersList).isNotNull();
        assertThat(allUsersList).hasSize(3);
        assertThat(allUsersList).containsExactlyElementsOf(usersList);

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void updateUserShouldReturnUpdatedUser() {
        //Given user id to update, existing user with this id and the expected updated user with new data
        Long userIdToUpdate = 1L;
        UserEntity existingUser = new UserEntity(userIdToUpdate, "John", "Doe", "john.doe@example.com");
        UserEntity expectedUpdatedUser = new UserEntity(userIdToUpdate, "UpdatedFirstName", "UpdatedLastName", "john.doe@example.com");

        // Simulate that findById and save methods of the repository performing the update procedure
        when(userRepository.findById(userIdToUpdate)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(UserEntity.class))).thenReturn(expectedUpdatedUser);

        // Perform the updateUser operation
        UserEntity updatedUser = userService.updateUserById(userIdToUpdate, expectedUpdatedUser);

        // Assert that the updated user is correct
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser).isEqualTo(expectedUpdatedUser);

        verify(userRepository, times(1)).findById(userIdToUpdate);
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }
}
