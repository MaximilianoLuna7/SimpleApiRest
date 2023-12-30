package com.apirest.controllers;

import com.apirest.models.UserEntity;
import com.apirest.servicies.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createUserShouldAddNewUser() throws Exception {
        // Given a user to be created
        UserEntity userToCreate = UserEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        when(userService.addUser(any(UserEntity.class))).thenReturn(userToCreate);

        // When making a POST request to "/api/users" with JSON payload
        ResultActions result = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userToCreate)));

        // Then expect a status of 201 and JSON object with the created user
        result.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        // Verify that the addUser method in the service was called exactly once
        verify(userService, times(1)).addUser(any(UserEntity.class));
    }

    @Test
    void getUserShouldReturnUser() throws Exception {
        // Given
        Long userIdToGet = 1L;
        UserEntity userToGet = UserEntity.builder()
                .id(userIdToGet)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        when(userService.getUserById(userIdToGet)).thenReturn(userToGet);

        // When
        ResultActions response = mockMvc.perform(get("/api/users/{userIdToCreate}", userIdToGet)
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userIdToGet))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        // Verify that the service method was called with the correct argument
        verify(userService, times(1)).getUserById(userIdToGet);
    }

    @Test
    void getAllUsersShouldReturnListOfUsers() throws Exception {
        // Given
        List<UserEntity> usersList = Arrays.asList(
                UserEntity.builder().id(1L).firstName("John").lastName("Doe").email("john.doe@example.com").build(),
                UserEntity.builder().id(2L).firstName("Mike").lastName("Smith").email("mike.smith@example.com").build()
        );

        when(userService.getAllUsers()).thenReturn(usersList);

        // When
        ResultActions result = mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(usersList.size()))
                .andExpect(jsonPath("$[0].id").value(usersList.get(0).getId()))
                .andExpect(jsonPath("$[0].firstName").value(usersList.get(0).getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(usersList.get(0).getLastName()))
                .andExpect(jsonPath("$[0].email").value(usersList.get(0).getEmail()))
                .andExpect(jsonPath("$[1].id").value(usersList.get(1).getId()))
                .andExpect(jsonPath("$[1].firstName").value(usersList.get(1).getFirstName()))
                .andExpect(jsonPath("$[1].lastName").value(usersList.get(1).getLastName()))
                .andExpect(jsonPath("$[1].email").value(usersList.get(1).getEmail()));
    }
}
