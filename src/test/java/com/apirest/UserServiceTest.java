package com.apirest;

import com.apirest.models.UserEntity;
import com.apirest.repositories.UserRepository;
import com.apirest.servicies.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
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
}
