package ru.practicum.shareit.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.shareit.user.model.User;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter
public class UserNotFoundException extends RuntimeException {
    private User user;

    public UserNotFoundException(String message) {
        super(message);
    }
}
