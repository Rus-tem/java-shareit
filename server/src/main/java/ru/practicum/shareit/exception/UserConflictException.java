package ru.practicum.shareit.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserConflictException extends RuntimeException {
    private User user;

    public UserConflictException(String message, User user) {
        super(message);
        this.user = user;
    }
}
