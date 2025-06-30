package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserConflictException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.UserValidationException;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private Long nextUserId = 1L;
    private static final String EMAIL_PATTERN =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    @Override
    //Получение списка всех пользователей
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Сохранение пользователя
    @Override
    public User saveUser(User user) {
        checkUser(user);
        checkDuplicateEmailUser(user);
        user.setId(nextUserId++);
        return userRepository.save(user);
    }

    // Обновление пользователя
    @Override
    public User updateUser(Long userId, User newUser) {

        User oldUser = getUserById(userId);
        if (newUser.getEmail() == null && newUser.getName() == null) {
            throw new UserNotFoundException("Не заполнены поля: Имя пользователя и Email");
        }
        if (newUser.getName() == null || newUser.getName().isBlank()) {
            newUser.setName(oldUser.getName());
        }
        if (newUser.getEmail() == null || newUser.getEmail().isBlank()) {
            newUser.setEmail(oldUser.getEmail());
        } else {
            checkDuplicateEmailUser(newUser);
        }
        return userRepository.update(oldUser, newUser);
    }

    //Поиск пользователя по ID
    @Override
    public User getUserById(Long userId) {
        Optional<User> foundUser = userRepository.findAll().stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst();
        if (foundUser.isPresent()) {
            return foundUser.get();
        } else {
            throw new UserNotFoundException("Такой пользователь не найден");
        }
    }

    // Удаление пользователя
    @Override
    public void deleteUser(Long userId) {
        userRepository.delete(getUserById(userId));
    }

    // Поиск пользователя по email
    private Optional<User> getUserByEmail(String email) {
        Optional<User> foundUser = userRepository.findAll().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
        return foundUser;
    }

    // Метод для проверки имени пользователя
    private void checkUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            throw new UserValidationException("Не корректное имя пользователя", user);
        }
        if (user.getEmail() == null || user.getEmail().isEmpty() || !isValidEmail(user.getEmail())) {
            throw new UserValidationException("Не корректный email", user);
        }
    }

    // Метод для проверки дубля email пользователя
    private void checkDuplicateEmailUser(User user) {
        if (getUserByEmail(user.getEmail()).isPresent()) {
            throw new UserConflictException("Данный email уже используется", user);
        }
    }

    // Метод проверки корректности email
    private boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}