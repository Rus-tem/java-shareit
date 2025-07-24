package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ShareItBookingTests {

    @Mock
    private BookingService bookingService;
    @Mock
    private BookingRepository bookingRepository;
    @InjectMocks
    private BookingController bookingController;

    @Test
    void getAllBooking() {
        List<Booking> expectedList = List.of(new Booking());
        Mockito.when(bookingRepository.findAll()).thenReturn(expectedList);
        List<Booking> actualList = bookingRepository.findAll();
        assertEquals(expectedList, actualList);
    }

    @Test
    void getBookingById() {
        Long bookingId = 0L;
        Booking expectedBooking = new Booking();
        bookingRepository.save(expectedBooking);
        Mockito.when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(expectedBooking));
        Booking actualBooking = bookingRepository.findById(bookingId).get();
        assertEquals(expectedBooking, actualBooking);
    }

    @Test
    void saveBooking() {
        Long bookingId = 0L;
        Booking bookingToSave = new Booking();
        bookingRepository.save(bookingToSave);
        Mockito.when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(bookingToSave));
        Booking actualBooking = bookingRepository.findById(bookingId).get();
        assertEquals(bookingToSave.getId(), actualBooking.getId());
    }
}
