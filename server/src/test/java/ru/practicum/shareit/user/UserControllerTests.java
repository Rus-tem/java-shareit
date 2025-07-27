package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.RequestUser;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTests {
    @Mock
    UserService userService;

    @InjectMocks
    UserController userController;

    @Autowired
    MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Test
    void addUser() throws Exception {
        RequestUser requestUser = new RequestUser(1L,"testName", "test@email.com");
        UserDto userDto = new UserDto(1L, "testName", "test@email.com");

        when(userService.saveUser(requestUser)).thenReturn(userDto);




    }
}
