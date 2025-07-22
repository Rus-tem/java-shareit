package ru.practicum.shareit.exception;

import lombok.Getter;
import ru.practicum.shareit.user.model.User;

@Getter
public class UserValidationException extends RuntimeException {
    private User user;

    public UserValidationException(String message, User user) {
        super(message);
        this.user = user;
    }
}
