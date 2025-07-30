package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    // Получение booking по ID
    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable("bookingId") Long bookingId) {
        return bookingService.getBooking(userId, bookingId);
    }

    // Получение booking по ownerId
    @GetMapping("/owner")
    public List<BookingDto> getBookingByOwner(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getBookingByOwner(userId);
    }

    // Получение всех Booking по userId
    @GetMapping
    public List<BookingDto> getAllBooking(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getAllBooking(userId);
    }

    // Сохранение Booking
    @PostMapping
    public BookingDto saveBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @RequestBody RequestBookingDto requestBookingDto) {
        return bookingService.saveBooking(userId, requestBookingDto);
    }

    // Подтверждение Booking
    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@PathVariable("bookingId") Long bookingId,
                                     @RequestHeader("X-Sharer-User-Id") Long userId,
                                     @RequestParam(value = "approved", required = false, defaultValue = "true") Boolean searchQuery) {
        return bookingService.approveBooking(bookingId, userId, searchQuery);
    }
}
