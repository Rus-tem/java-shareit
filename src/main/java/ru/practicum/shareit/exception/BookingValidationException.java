package ru.practicum.shareit.exception;

import lombok.Getter;
import ru.practicum.shareit.booking.model.Booking;

@Getter
public class BookingValidationException extends RuntimeException {
    private Booking booking;

    public BookingValidationException(String message) {
        super(message);

    }
}
