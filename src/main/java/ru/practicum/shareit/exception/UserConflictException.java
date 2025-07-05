package ru.practicum.shareit.exception;

import lombok.Getter;
import ru.practicum.shareit.user.model.User;

@Getter
public class UserConflictException extends RuntimeException {
    User user;

    public UserConflictException(String message, User user) {
        super(message);
        this.user = user;
    }
}
