package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.UserConflictException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.UserValidationException;
import ru.practicum.shareit.user.dto.RequestUser;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Spy
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getAllUsers() {
        List<User> users = List.of(
                new User(1L, "Alice", "alice@example.com"),
                new User(2L, "Bob", "bob@example.com")
        );

        List<UserDto> expectedDtos = List.of(
                new UserDto(1L, "alice@example.com", "Alice"),
                new UserDto(2L, "bob@example.com", "Bob")
        );

        when(userRepository.findAll()).thenReturn(users);

        try (MockedStatic<UserMapper> mockedStatic = Mockito.mockStatic(UserMapper.class)) {
            mockedStatic.when(() -> UserMapper.mapToUserDto(users.get(0)))
                    .thenReturn(expectedDtos.get(0));
            mockedStatic.when(() -> UserMapper.mapToUserDto(users.get(1)))
                    .thenReturn(expectedDtos.get(1));

            List<UserDto> result = userService.getAllUsers();

            assertEquals(2, result.size());
            assertEquals("Alice", result.get(0).getName());
            assertEquals("Bob", result.get(1).getName());

            verify(userRepository).findAll();
        }
    }

    @Test
    void saveUser() {
        RequestUser requestUser = new RequestUser();
        requestUser.setName("John Doe");
        requestUser.setEmail("john@example.com");

        User mappedUser = new User(null, "John Doe", "john@example.com");

        User userWithId = new User(1L, "John Doe", "john@example.com");

        UserDto expectedDto = new UserDto(1L, "John Doe", "john@example.com");

        try (MockedStatic<UserMapper> mockedStatic = Mockito.mockStatic(UserMapper.class)) {
            mockedStatic.when(() -> UserMapper.requestUserMapToUser(requestUser))
                    .thenReturn(mappedUser);
            mockedStatic.when(() -> UserMapper.mapToUserDto(any(User.class)))
                    .thenReturn(expectedDto);

            when(userRepository.save(any(User.class))).thenReturn(userWithId);

            doNothing().when(userService).checkUser(any(User.class));
            doNothing().when(userService).checkDuplicateEmailUser(any(User.class));

            UserDto result = userService.saveUser(requestUser);

            assertNotNull(result);
            assertEquals(expectedDto.getName(), result.getName());
            assertEquals(expectedDto.getEmail(), result.getEmail());

            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            verify(userRepository).save(userCaptor.capture());
            assertEquals(1L, userCaptor.getValue().getId());

            verify(userService).checkUser(mappedUser);
            verify(userService).checkDuplicateEmailUser(mappedUser);
        }

    }

    @Test
    void saveUser_shouldThrowValidationException_whenNameIsBlank() {
        RequestUser requestUser = new RequestUser(null, " ", "valid@example.com");

        doCallRealMethod().when(userService).checkUser(any(User.class)); // Вызвать реальный метод

        UserValidationException ex = assertThrows(UserValidationException.class, () -> {
            userService.saveUser(requestUser);
        });

        assertTrue(ex.getMessage().contains("Не корректное имя пользователя"));
    }

    @Test
    void saveUser_shouldThrowValidationException_whenEmailInvalid() {
        RequestUser requestUser = new RequestUser(null, "Valid Name", "invalid-email");

        doCallRealMethod().when(userService).checkUser(any(User.class));

        UserValidationException ex = assertThrows(UserValidationException.class, () -> {
            userService.saveUser(requestUser);
        });

        assertTrue(ex.getMessage().contains("Не корректный email"));
    }

    @Test
    void saveUser_shouldThrowConflictException_whenEmailDuplicate() {
        RequestUser requestUser = new RequestUser(null, "Valid Name", "duplicate@example.com");
        User user = new User(null, "Valid Name", "duplicate@example.com");

        doNothing().when(userService).checkUser(any(User.class)); // пропускаем проверку имени/емейла
        doCallRealMethod().when(userService).checkDuplicateEmailUser(any(User.class)); // реальный метод

        when(userRepository.findAll()).thenReturn(List.of(user));

        UserConflictException ex = assertThrows(UserConflictException.class, () -> {
            userService.saveUser(requestUser);
        });

        assertTrue(ex.getMessage().contains("Данный email уже используется"));
    }

    @Test
    void updateUser() {
        Long userId = 1L;

        // Входные данные
        RequestUser requestUser = new RequestUser();
        requestUser.setName("Updated Name");
        requestUser.setEmail("updated@example.com");

        User oldUser = new User(userId, "Old Name", "old@example.com");
        UserDto oldUserDto = new UserDto(userId, "Old Name", "old@example.com");

        User newUser = new User(null, "Updated Name", "updated@example.com");
        User savedUser = new User(userId, "Updated Name", "updated@example.com");
        UserDto savedUserDto = new UserDto(userId, "updated@example.com", "Updated Name");

        doReturn(oldUserDto).when(userService).getUserById(userId);

        doNothing().when(userService).checkDuplicateEmailUser(any(User.class));

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        try (MockedStatic<UserMapper> mockedStatic = Mockito.mockStatic(UserMapper.class)) {
            mockedStatic.when(() -> UserMapper.requestUserMapToUser(requestUser))
                    .thenReturn(newUser);

            mockedStatic.when(() -> UserMapper.mapToUserDto(savedUser))
                    .thenReturn(savedUserDto);

            UserDto result = userService.updateUser(userId, requestUser);

            assertNotNull(result);
            assertEquals("Updated Name", result.getName());
            assertEquals("updated@example.com", result.getEmail());

            verify(userRepository).save(any(User.class));
            verify(userService).checkDuplicateEmailUser(any(User.class));
        }
    }

    @Test
    void updateUser_shouldThrowUserNotFoundException_whenNameAndEmailNull() {
        Long userId = 1L;
        RequestUser requestUser = new RequestUser(null, null, null);

        UserDto oldUserDto = new UserDto(userId, "old@example.com", "Old Name");
        doReturn(oldUserDto).when(userService).getUserById(userId);

        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> {
            userService.updateUser(userId, requestUser);
        });

        assertEquals("Не заполнены поля: Имя пользователя и Email", ex.getMessage());
    }

    @Test
    void getUserById_shouldReturnUserDto_whenUserExists() {
        Long userId = 1L;
        User user = new User(userId, "Alice", "alice@example.com");
        UserDto expectedDto = new UserDto(userId, "alice@example.com", "Alice");

        when(userRepository.findAll()).thenReturn(List.of(user));

        try (MockedStatic<UserMapper> mockedStatic = Mockito.mockStatic(UserMapper.class)) {
            mockedStatic.when(() -> UserMapper.mapToUserDto(user)).thenReturn(expectedDto);

            UserDto result = userService.getUserById(userId);

            assertNotNull(result);
            assertEquals("Alice", result.getName());
            assertEquals("alice@example.com", result.getEmail());

            verify(userRepository).findAll();
        }
    }

    @Test
    void getUserById_shouldThrowException_whenUserNotFound() {
        Long userId = 99L;

        when(userRepository.findAll()).thenReturn(List.of(
                new User(1L, "Bob", "bob@example.com")
        ));

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.getUserById(userId)
        );

        assertEquals("Такой пользователь не найден", exception.getMessage());
    }

    @Test
    void deleteUser() {
        Long userId = 1L;

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }
}