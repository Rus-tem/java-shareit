package ru.practicum.shareit.booking.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {

    public static BookingDto mapToBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setItem(booking.getItem());
        bookingDto.setBooker(booking.getBooker());
        return bookingDto;
    }

    public static Booking mapToBooking(RequestBookingDto requestBookingDto, Item item, User booker) {
        Booking booking = new Booking();
        booking.setStart(requestBookingDto.getStart());
        booking.setEnd(requestBookingDto.getEnd());
        booking.setItem(item);
        booking.setBooker(booker);
        return booking;
    }


}
