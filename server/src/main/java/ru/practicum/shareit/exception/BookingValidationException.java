package ru.practicum.shareit.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Booking;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookingValidationException extends RuntimeException {
    private Booking booking;

    public BookingValidationException(String message) {
        super(message);

    }
}
