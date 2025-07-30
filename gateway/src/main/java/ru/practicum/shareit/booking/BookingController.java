package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.RequestBookingDto;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingClient bookingClient;

    // Получение booking по ID
    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable("bookingId") Long bookingId) {
        return bookingClient.getBooking(userId, bookingId);
    }

    // Получение booking по ownerId
    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingByOwner(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingClient.getBookingByOwner(userId);
    }

    // Получение всех Booking по userId
    @GetMapping
    public ResponseEntity<Object> getAllBooking(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingClient.getAllBooking(userId);
    }

    // Сохранение Booking
    @PostMapping
    public ResponseEntity<Object> saveBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestBody RequestBookingDto requestBookingDto) {
        return bookingClient.saveBooking(userId, requestBookingDto);
    }

    // Подтверждение Booking
    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@PathVariable("bookingId") Long bookingId,
                                                 @RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @RequestParam(value = "approved", required = false) Boolean searchQuery) {
        return bookingClient.approveBooking(bookingId, userId, searchQuery);
    }
}
