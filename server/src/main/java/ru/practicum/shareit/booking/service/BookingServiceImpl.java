package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BookingValidationException;
import ru.practicum.shareit.exception.ItemValidationException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final ItemService itemService;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private Long nextBookingId = 1L;

    //Получение Booking по ID
    @Override
    public BookingDto getBooking(Long userId, Long bookingId) {
        User booker = UserMapper.mapToUser(userService.getUserById(userId));
        Booking booking = bookingRepository.findById(bookingId).get();
        return BookingMapper.mapToBookingDto(booking);
    }

    // Получение списка Booking по owner
    @Override
    public List<BookingDto> getBookingByOwner(Long userId) {
        User booker = UserMapper.mapToUser(userService.getUserById(userId));
        List<Booking> listOfBooking = bookingRepository.getAllBooking(booker);
        return listOfBooking.stream().map(BookingMapper::mapToBookingDto).toList();
    }

    // Получение списка Booking по userId
    @Override
    public List<BookingDto> getAllBooking(Long userId) {
        User user = UserMapper.mapToUser(userService.getUserById(userId));
        List<Booking> listOfBooking = bookingRepository.getAllBooking(user);/// конец изначальный
        return listOfBooking.stream().map(BookingMapper::mapToBookingDto).toList();

    }

    //Сохранение Booking
    @Override
    public BookingDto saveBooking(Long userId, RequestBookingDto requestBookingDto) {
        User booker = UserMapper.mapToUser(userService.getUserById(userId));
        Item item = ItemMapper.mapToItem(itemService.getItemByItemId(requestBookingDto.getItemId(), userId));
        Booking booking = BookingMapper.mapToBooking(requestBookingDto, item, booker);
        checkBooking(booking);
        booking.setId(nextBookingId++);
        booking.setStatus(Status.WAITING);
        bookingRepository.save(booking);
        return BookingMapper.mapToBookingDto(booking);
    }

    //Подтверждение Booking
    @Override
    public BookingDto approveBooking(Long bookingId, Long userId, Boolean searchQuery) {
        Booking booking = bookingRepository.findById(bookingId).get();
        if ((booking.getItem().getOwner().getId() == userId) && searchQuery) {
            booking.setStatus(Status.APPROVED);
            bookingRepository.save(booking);
        } else if ((booking.getItem().getOwner().getId() == userId) && !searchQuery) {
            booking.setStatus(Status.REJECTED);
            bookingRepository.save(booking);
        } else {
            throw new ItemValidationException("Данный пользователь не является хозяином для данного Item");
        }
        return BookingMapper.mapToBookingDto(booking);
    }

    //Проверка Booking
    private void checkBooking(Booking booking) {
        if (!booking.getItem().getAvailable()) {
            throw new ItemValidationException("Данный item не доступен");
        }
        if (booking.getEnd().isBefore(booking.getStart()) || booking.getStart().equals(booking.getEnd()) || booking.getStart().isAfter(booking.getEnd())
            || booking.getStart() == null || booking.getEnd() == null) {
            throw new BookingValidationException("Неправильно указано время Booking");
        }
    }
}
