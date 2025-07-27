package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserController userController;


    @Test
    void getAllUsers() {
    }

    @Test
    void saveUser() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void getUserById() {
    }

    @Test
    void deleteUser() {
    }
}