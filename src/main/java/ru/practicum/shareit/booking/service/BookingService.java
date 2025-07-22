package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.RequestBookingDto;

import java.util.List;

public interface BookingService {

    BookingDto getBooking(Long userId, Long bookerOrOwnerId);

    List<BookingDto> getAllBooking(Long userId);

    BookingDto saveBooking(Long userId, RequestBookingDto requestBookingDto);

    BookingDto approveBooking(Long bookingI, Long userId, Boolean searchQuery);

    List<BookingDto> getBookingByOwner(Long userId);

}
