package ru.practicum.shareit.user;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.RequestUser;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void getAllUsers_shouldReturnListOfUsers() throws Exception {
        List<UserDto> users = List.of(new UserDto(1L, "test@example.com", "Test User"));

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(users.size())))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].email", is("test@example.com")))
                .andExpect(jsonPath("$[0].name", is("Test User")));
    }

    @Test
    void saveUser_shouldReturnCreatedUser() throws Exception {
        RequestUser requestUser = new RequestUser(null, "Test User", "test@example.com");
        UserDto userDto = new UserDto(1L, "test@example.com", "Test User");

        when(userService.saveUser(requestUser)).thenReturn(userDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is("test@example.com")))
                .andExpect(jsonPath("$.name", is("Test User")));
    }

    @Test
    void updateUser_shouldReturnUpdatedUser() throws Exception {
        long userId = 1L;
        RequestUser updateRequest = new RequestUser(null, "Updated Name", "updated@example.com");
        UserDto updatedUser = new UserDto(userId, "updated@example.com", "Updated Name");

        when(userService.updateUser(eq(userId), any())).thenReturn(updatedUser);

        mockMvc.perform(patch("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) userId)))
                .andExpect(jsonPath("$.name", is("Updated Name")))
                .andExpect(jsonPath("$.email", is("updated@example.com")));
    }

    @Test
    void getUserById_shouldReturnUser() throws Exception {
        long userId = 1L;
        UserDto userDto = new UserDto(userId, "test@example.com", "Test User");

        when(userService.getUserById(userId)).thenReturn(userDto);

        mockMvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) userId)))
                .andExpect(jsonPath("$.email", is("test@example.com")))
                .andExpect(jsonPath("$.name", is("Test User")));
    }

    @Test
    void deleteUser_shouldReturnOk() throws Exception {
        long userId = 1L;

        doNothing().when(userService).deleteUser(userId);

        mockMvc.perform(delete("/users/{userId}", userId))
                .andExpect(status().isOk());
    }

}