package ru.practicum.shareit.exception;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

@RestController
@RequestMapping("/test")
public class TestExceptionController {

    @GetMapping("/user-validation")
    public void throwUserValidation() {
        User user = new User();
        user.setId(1L);
        throw new UserValidationException("Invalid user data", user);
    }

    @GetMapping("/user-conflict")
    public void throwUserConflict() {
        User user = new User();
        user.setId(1L);
        throw new UserConflictException("User conflict occurred", user);
    }

    @GetMapping("/user-not-found")
    public void throwUserNotFound() {
        throw new UserNotFoundException("User not found");
    }

    @GetMapping("/item-validation")
    public void throwItemValidation() {
        Item item = new Item();
        item.setId(1L);
        throw new ItemValidationException("Invalid Item");
    }

    @GetMapping("/item-not-found")
    public void throwItemNotFound() {
        Item item = new Item();
        item.setId(2L);
        throw new ItemNotFoundException("Item not found");
    }

    @GetMapping("/booking-validation")
    public void throwBookingValidation() {
        Booking booking = new Booking();
        booking.setId(1L);
        throw new BookingValidationException("Invalid booking");
    }

    @GetMapping("/null-found")
    public void throwNullFound() {
        Booking booking = new Booking();
        booking.setId(2L);
        throw new NullFoundException("Null value encountered");
    }

    @GetMapping("/request-not-found")
    public void throwRequestNotFound() {
        Request request = new Request();
        request.setId(3L);
        throw new RequestNotFoundException("Request not found");
    }
}
