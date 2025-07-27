package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ShareItUserTests {
    @Mock
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserController userController;


    @Test
    void getAllUser() {
        List<User> expectedList = List.of(new User());
        Mockito.when(userRepository.findAll()).thenReturn(expectedList);
        List<User> actualList = userRepository.findAll();
        assertEquals(expectedList, actualList);
    }

    @Test
    void getUserById() {
        Long userId = 0L;
        User expectedUser = new User();
        userRepository.save(expectedUser);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));
        User actualUser = userRepository.findById(userId).get();
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void saveUser() {
        Long userId = 0L;
        User userToSave = new User(1L, "user1", "user1@user1.ru");
        userRepository.save(userToSave);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(userToSave));
        User actualUser = userRepository.findById(userId).get();
        assertEquals(actualUser.getId(), userToSave.getId());
        assertEquals(actualUser.getName(), userToSave.getName());
        assertEquals(actualUser.getEmail(), userToSave.getEmail());
    }

}
