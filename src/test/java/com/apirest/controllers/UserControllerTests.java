package com.apirest.controllers;

import com.apirest.models.UserEntity;
import com.apirest.servicies.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Create user - Successful")
    public void createUser_Successful() throws Exception {
        // Arrange
        UserEntity user = createUser();

        // Act & Assert
        mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status().isOk())
                .andExpect(content().string("User created successfully"));

        verify(userService, times(1)).addUser(user);
    }

    @Test
    @DisplayName("Get user - Successful")
    public void getUser_Successful() throws Exception {
        // Arrange
        UserEntity user = createUser();
        Long userId = user.getId();

        when(userService.getUserById(userId)).thenReturn(user);

        // Act & Assert
        mockMvc.perform(get("/api/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));

        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    @DisplayName("Get all users - Successful")
    public void getAllUsers_Successful() throws Exception {
        // Arrange
        List<UserEntity> userList = new ArrayList<>();

        UserEntity user1 = createUser();
        UserEntity user2 = UserEntity.builder()
                .id(2L)
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .build();

        userList.add(user1);
        userList.add(user2);

        when(userService.getAllUsers()).thenReturn(userList);

        // Act & Assert
        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(userList.size())))
                .andExpect(jsonPath("$[0].id").value(userList.get(0).getId()))
                .andExpect(jsonPath("$[0].firstName").value(userList.get(0).getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(userList.get(0).getLastName()))
                .andExpect(jsonPath("$[0].email").value(userList.get(0).getEmail()))

                .andExpect(jsonPath("$[1].id").value(userList.get(1).getId()))
                .andExpect(jsonPath("$[1].firstName").value(userList.get(1).getFirstName()))
                .andExpect(jsonPath("$[1].lastName").value(userList.get(1).getLastName()))
                .andExpect(jsonPath("$[1].email").value(userList.get(1).getEmail()));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("Update user - Successful")
    public void updateUser_Successful() throws Exception {
        // Arrange
        UserEntity updatedUser = createUser();
        Long userId = updatedUser.getId();

        // Act & Assert
        mockMvc.perform(put("/api/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(content().string("User updated successfully"));

        verify(userService, times(1)).updateUserById(userId, updatedUser);
    }

    @Test
    @DisplayName("Delete user - Successful")
    public void deleteUser_Successful() throws Exception {
        // Arrange
        Long userId = 1L;

        // Act & Assert
        mockMvc.perform(delete("/api/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));

        verify(userService, times(1)).deleteUser(userId);
    }

    private UserEntity createUser() {
        return UserEntity.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}
