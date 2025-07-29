package ru.practicum.shareit.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserValidationException extends RuntimeException {
    private User user;

    public UserValidationException(String message, User user) {
        super(message);
        this.user = user;
    }
}
