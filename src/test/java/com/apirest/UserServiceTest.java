package com.apirest;

import com.apirest.models.UserEntity;
import com.apirest.repositories.UserRepository;
import com.apirest.servicies.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void addUserShouldAddANewUser() {
        UserEntity userToAdd = UserEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        when(userRepository.save(userToAdd)).thenReturn(userToAdd);

        UserEntity AddedUser = userService.addUser(userToAdd);

        assertThat(AddedUser).isEqualTo(userToAdd);
    }
}
