package com.apirest;

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
}
