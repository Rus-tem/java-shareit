package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.booker = :booker")
    List<Booking> getAllBooking(@Param("booker") User booker);

    @Query("SELECT b FROM Booking b WHERE b.booker = :booker")
    List<Booking> getAllBookingByOwner(@Param("booker") User booker);

}


