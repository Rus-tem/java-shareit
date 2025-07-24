package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.util.Map;

@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(UserValidationException.class)
    public ResponseEntity<Map<String, User>> handleUserValidationException(UserValidationException ex) {
        Map<String, User> error = Map.of(ex.getMessage(), ex.getUser());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserConflictException.class)
    public ResponseEntity<Map<String, User>> handleUserConflictException(UserConflictException ex) {
        Map<String, User> error = Map.of(ex.getMessage(), ex.getUser());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, User>> handleUserNonFoundException(UserNotFoundException ex) {
        Map<String, User> error = Map.of(ex.getMessage(), ex.getUser());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ItemValidationException.class)
    public ResponseEntity<Map<String, Item>> handleItemValidationException(ItemValidationException ex) {
        Map<String, Item> error = Map.of(ex.getMessage(), ex.getItem());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<Map<String, Item>> handleItemNonFoundException(ItemNotFoundException ex) {
        Map<String, Item> error = Map.of(ex.getMessage(), ex.getItem());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookingValidationException.class)
    public ResponseEntity<Map<String, Booking>> handleBookingValidationException(BookingValidationException ex) {
        Map<String, Booking> error = Map.of(ex.getMessage(), ex.getBooking());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullFoundException.class)
    public ResponseEntity<Map<String, Booking>> handleNullFoundValidationException(NullFoundException ex) {
        Map<String, Booking> error = Map.of(ex.getMessage(), ex.getBooking());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RequestNotFoundException.class)
    public ResponseEntity<Map<String, Request>> handleRequestNotFoundException(RequestNotFoundException ex) {
        Map<String, Request> error = Map.of(ex.getMessage(), ex.getRequest());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}