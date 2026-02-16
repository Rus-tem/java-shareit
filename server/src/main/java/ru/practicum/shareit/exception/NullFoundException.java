package ru.practicum.shareit.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.shareit.booking.model.Booking;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NullFoundException extends RuntimeException {
    private Booking booking;

    public NullFoundException(String message) {
        super(message);
    }
}
