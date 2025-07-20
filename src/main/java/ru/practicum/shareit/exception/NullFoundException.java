package ru.practicum.shareit.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.shareit.booking.model.Booking;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
@Getter
public class NullFoundException extends RuntimeException {
    Booking booking;

    public NullFoundException(String message) {
        super(message);
    }
}
