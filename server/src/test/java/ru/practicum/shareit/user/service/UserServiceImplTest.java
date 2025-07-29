package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.UserNotFoundException;
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
        // Подготовка исходных данных
        List<User> users = List.of(
                new User(1L, "Alice", "alice@example.com"),
                new User(2L, "Bob", "bob@example.com")
        );

        List<UserDto> expectedDtos = List.of(
                new UserDto(1L, "alice@example.com", "Alice"),
                new UserDto(2L, "bob@example.com", "Bob")
        );

        // Мокаем findAll()
        when(userRepository.findAll()).thenReturn(users);

        // Мокаем статические методы
        try (MockedStatic<UserMapper> mockedStatic = Mockito.mockStatic(UserMapper.class)) {
            mockedStatic.when(() -> UserMapper.mapToUserDto(users.get(0)))
                    .thenReturn(expectedDtos.get(0));
            mockedStatic.when(() -> UserMapper.mapToUserDto(users.get(1)))
                    .thenReturn(expectedDtos.get(1));

            // Вызываем метод
            List<UserDto> result = userService.getAllUsers();

            // Проверки
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

        // Пользователь до сохранения (без ID)
        User mappedUser = new User(null, "John Doe", "john@example.com");

        // Ожидаемый пользователь после установки ID
        User userWithId = new User(1L, "John Doe", "john@example.com");

        // DTO, который должен вернуться
        UserDto expectedDto = new UserDto(1L, "John Doe", "john@example.com");

        // Мокаем статические методы UserMapper
        try (MockedStatic<UserMapper> mockedStatic = Mockito.mockStatic(UserMapper.class)) {
            mockedStatic.when(() -> UserMapper.requestUserMapToUser(requestUser))
                    .thenReturn(mappedUser);
            mockedStatic.when(() -> UserMapper.mapToUserDto(any(User.class)))
                    .thenReturn(expectedDto);

            // Мокаем поведение userRepository.save
            when(userRepository.save(any(User.class))).thenReturn(userWithId);

            // Мокаем внутренние методы, чтобы не вызывались реальные
            doNothing().when(userService).checkUser(any(User.class));
            doNothing().when(userService).checkDuplicateEmailUser(any(User.class));

            // Вызов метода
            UserDto result = userService.saveUser(requestUser);

            // Проверки
            assertNotNull(result);
            assertEquals(expectedDto.getName(), result.getName());
            assertEquals(expectedDto.getEmail(), result.getEmail());

            // Проверка вызова save с пользователем, которому присвоен ID
            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            verify(userRepository).save(userCaptor.capture());
            assertEquals(1L, userCaptor.getValue().getId());

            // Проверка вызова внутренних методов
            verify(userService).checkUser(mappedUser);
            verify(userService).checkDuplicateEmailUser(mappedUser);
        }

    }

    @Test
    void updateUser() {
        Long userId = 1L;

        // Входные данные
        RequestUser requestUser = new RequestUser();
        requestUser.setName("Updated Name");
        requestUser.setEmail("updated@example.com");

        // Старый пользователь в базе
        User oldUser = new User(userId, "Old Name", "old@example.com");
        UserDto oldUserDto = new UserDto(userId, "Old Name", "old@example.com");

        // Новый пользователь после маппинга
        User newUser = new User(null, "Updated Name", "updated@example.com");
        User savedUser = new User(userId, "Updated Name", "updated@example.com");
        UserDto savedUserDto = new UserDto(userId, "updated@example.com", "Updated Name");

        // Мокаем getUserById внутри userService
        doReturn(oldUserDto).when(userService).getUserById(userId);

        // Мокаем checkDuplicateEmailUser (ничего не делает)
        doNothing().when(userService).checkDuplicateEmailUser(any(User.class));

        // Мокаем сохранение
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Мокаем статические методы UserMapper
        try (MockedStatic<UserMapper> mockedStatic = Mockito.mockStatic(UserMapper.class)) {
            mockedStatic.when(() -> UserMapper.requestUserMapToUser(requestUser))
                    .thenReturn(newUser);

            mockedStatic.when(() -> UserMapper.mapToUserDto(savedUser))
                    .thenReturn(savedUserDto);

            // Вызываем метод
            UserDto result = userService.updateUser(userId, requestUser);

            // Проверки
            assertNotNull(result);
            assertEquals("Updated Name", result.getName());
            assertEquals("updated@example.com", result.getEmail());

            // Проверка, что методы были вызваны
            verify(userRepository).save(any(User.class));
            verify(userService).checkDuplicateEmailUser(any(User.class));
        }
    }

    @Test
    void getUserById_shouldReturnUserDto_whenUserExists() {
        Long userId = 1L;
        User user = new User(userId, "Alice", "alice@example.com");
        UserDto expectedDto = new UserDto(userId, "alice@example.com", "Alice");

        // Мокаем поведение
        when(userRepository.findAll()).thenReturn(List.of(user));

        // Мокаем статический UserMapper
        try (MockedStatic<UserMapper> mockedStatic = Mockito.mockStatic(UserMapper.class)) {
            mockedStatic.when(() -> UserMapper.mapToUserDto(user)).thenReturn(expectedDto);

            // Вызов метода
            UserDto result = userService.getUserById(userId);

            // Проверка результата
            assertNotNull(result);
            assertEquals("Alice", result.getName());
            assertEquals("alice@example.com", result.getEmail());

            verify(userRepository).findAll();
        }
    }

    @Test
    void getUserById_shouldThrowException_whenUserNotFound() {
        Long userId = 99L;

        // Репозиторий возвращает список, но без нужного ID
        when(userRepository.findAll()).thenReturn(List.of(
                new User(1L, "Bob", "bob@example.com")
        ));

        // Проверка выброса исключения
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.getUserById(userId)
        );

        assertEquals("Такой пользователь не найден", exception.getMessage());
    }

    @Test
    void deleteUser() {
        Long userId = 1L;

        // Вызов метода
        userService.deleteUser(userId);

        // Проверка, что deleteById вызвался один раз с нужным ID
        verify(userRepository, times(1)).deleteById(userId);
    }
}