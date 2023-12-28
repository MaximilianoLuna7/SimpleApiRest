package com.apirest;

import com.apirest.models.UserEntity;
import com.apirest.repositories.UserRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DataJpaTest
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    private UserEntity firstUser;

    @BeforeEach
    void setUp() {
        firstUser = UserEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();
    }

    @Test
    void saveShouldSaveUserEntity() {
        // Given user to save

        // When save user
        UserEntity savedUser = userRepository.save(firstUser);
        // Then assert
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isGreaterThan(0);
        System.out.println(savedUser);
    }

    @Test
    void saveShouldThrowExceptionForExistingUser() {
        // Given existent user and user to save
        UserEntity existentUser = userRepository.save(firstUser);
        UserEntity userToSave = UserEntity.builder()
                .firstName("Mike")
                .lastName("Smith")
                .email("john.doe@example.com")
                .build();

        // When attempting to save user with an existing email, then throw an exception
        assertThatThrownBy(() -> userRepository.save(userToSave))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

}
