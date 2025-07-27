package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.RequestUser;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto saveUser(RequestUser requestUser);

    UserDto updateUser(Long userId, RequestUser requestUser);

    UserDto getUserById(Long userId);

    void deleteUser(Long userId);
}
