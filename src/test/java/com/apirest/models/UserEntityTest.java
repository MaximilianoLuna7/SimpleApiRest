package com.apirest.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class UserEntityTest {
    private UserEntity user;
    private final Long userId = 1L;
    private final String firstName = "John";
    private final String lastName = "Doe";
    private final String email = "john.doe@example.com";

    @BeforeEach
    public void setUp() {
        user = new UserEntity();
    }

    @Test
    @DisplayName("Create User instance")
    public void createUser_SuccessfulInstantiation() {
        // Act & Assert
        assertThat(user).isNotNull();
    }

    @Test
    @DisplayName("Set and get 'userId' property - Successful")
    public void setAndGetId_Successful() {
        // Arrange & Act
        user.setId(userId);

        // Assert
        assertThat(user.getId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("Set and get 'firstName' property - Successful")
    public void setAndGetFirstName_Successful() {
        // Arrange & Act
        user.setFirstName(firstName);

        // Assert
        assertThat(user.getFirstName()).isEqualTo(firstName);
    }

    @Test
    @DisplayName("Set and get 'lastName' property - Successful")
    public void setAndGetLastName_Successful() {
        // Arrange & Act
        user.setLastName(lastName);

        // Assert
        assertThat(user.getLastName()).isEqualTo(lastName);
    }

    @Test
    @DisplayName("Set and get 'email' property - Successful")
    public void setAndGetEmail_Successful() {
        // Arrange & Act
        user.setEmail(email);

        // Assert
        assertThat(user.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("Equality test")
    public void equalityTest() {
        // Arrange
        user.setId(userId);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);

        // Act
        UserEntity equalUser = new UserEntity(userId, firstName, lastName, email);

        // Assert
        assertThat(user).isEqualTo(equalUser);
    }

    @Test
    @DisplayName("Builder test")
    public void builderTest() {
        // Arrange & Act
        UserEntity builtUser = UserEntity.builder()
                .id(userId)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .build();

        // Assert
        assertThat(builtUser.getId()).isEqualTo(userId);
        assertThat(builtUser.getFirstName()).isEqualTo(firstName);
        assertThat(builtUser.getLastName()).isEqualTo(lastName);
        assertThat(builtUser.getEmail()).isEqualTo(email);
    }
}