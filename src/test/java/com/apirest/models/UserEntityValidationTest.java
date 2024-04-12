package com.apirest.models;

import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class UserEntityValidationTest {
    private LocalValidatorFactoryBean validatorFactoryBean;

    private final String blankString = "";
    private final String invalidEmail = "john.doe";

    @BeforeEach
    public void setUp() {
        validatorFactoryBean = new LocalValidatorFactoryBean();
        validatorFactoryBean.afterPropertiesSet();
    }

    @AfterEach
    public void tearDown() {
        if (validatorFactoryBean != null) {
            validatorFactoryBean.destroy();
        }
    }

    public Set<ConstraintViolation<UserEntity>> validate(UserEntity userToValidate) {
        return validatorFactoryBean.validate(userToValidate);
    }

    @Test
    @DisplayName("Validate 'firstName' property - Not blank")
    public void validateFirstName_NotBlank() {
        // Arrange
        UserEntity user = new UserEntity();

        // Act
        user.setFirstName(blankString);
        Set<ConstraintViolation<UserEntity>> violations = validate(user);

        // Assert
        assertThat(violations)
                .isNotEmpty()
                .extracting(ConstraintViolation::getMessage)
                .contains("First name must not be blank");
    }

    @Test
    @DisplayName("Validate 'lastName' property - Not blank")
    public void validateLastName_NotBlank() {
        // Arrange
        UserEntity user = new UserEntity();

        // Act
        user.setLastName(blankString);
        Set<ConstraintViolation<UserEntity>> violations = validate(user);

        // Assert
        assertThat(violations)
                .isNotEmpty()
                .extracting(ConstraintViolation::getMessage)
                .contains("Last name must not be blank");
    }

    @Test
    @DisplayName("Validate 'email' property - Not blank")
    public void validateEmail_NotBlank() {
        // Arrange
        UserEntity user = new UserEntity();

        // Act
        user.setEmail(blankString);
        Set<ConstraintViolation<UserEntity>> violations = validate(user);

        // Assert
        assertThat(violations)
                .isNotEmpty()
                .extracting(ConstraintViolation::getMessage)
                .contains("Email must not be blank");
    }

    @Test
    @DisplayName("Validate 'email' property - Format")
    public void validateEmail_Format() {
        // Arrange
        UserEntity user = new UserEntity();

        // Act
        user.setEmail(invalidEmail);
        Set<ConstraintViolation<UserEntity>> violations = validate(user);

        // Assert
        assertThat(violations)
                .isNotEmpty()
                .extracting(ConstraintViolation::getMessage)
                .contains("Invalid email format");
    }
}
