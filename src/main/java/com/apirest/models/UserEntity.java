package com.apirest.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotBlank(message = "First name must not be blank")
    @Column(name = "first_name")
    private String firstName;

    @NotBlank(message = "Last name must not be blank")
    @Column(name = "last_name")
    private String lastName;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Invalid email format")
    @Column(name = "email", unique = true)
    private String email;
}
