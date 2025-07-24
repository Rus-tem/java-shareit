package ru.practicum.shareit.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.shareit.request.model.Request;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter
public class RequestNotFoundException extends RuntimeException {
    private Request request;

    public RequestNotFoundException(String message) {
        super(message);
    }
}
