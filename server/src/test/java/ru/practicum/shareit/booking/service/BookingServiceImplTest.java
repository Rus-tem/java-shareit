package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ItemValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    @Mock
    private ItemService itemService;
    @Mock
    private UserService userService;
    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private User user;
    private Item item;
    private Booking booking;
    private RequestBookingDto requestBookingDto;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1L);
        user.setName("User");

        item = new Item();
        item.setId(1L);
        item.setName("Item");
        item.setAvailable(true);
        item.setOwner(user);

        booking = new Booking();
        booking.setId(1L);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setStatus(Status.WAITING);

        requestBookingDto = new RequestBookingDto();
        requestBookingDto.setItemId(1L);
        requestBookingDto.setStart(booking.getStart());
        requestBookingDto.setEnd(booking.getEnd());
    }

    @Test
    void saveBooking_success() {
        UserDto userDto = new UserDto(1L, "User", "email@test.com");
        ItemDto itemDto = new ItemDto();
        BookingDto bookingDto = new BookingDto();

        when(userService.getUserById(1L)).thenReturn(userDto);
        when(itemService.getItemByItemId(1L, 1L)).thenReturn(itemDto);
        when(bookingRepository.save(any())).thenReturn(booking);

        try (
                MockedStatic<UserMapper> userMapperMocked = mockStatic(UserMapper.class);
                MockedStatic<ItemMapper> itemMapperMocked = mockStatic(ItemMapper.class);
                MockedStatic<BookingMapper> bookingMapperMocked = mockStatic(BookingMapper.class)
        ) {
            userMapperMocked.when(() -> UserMapper.mapToUser(userDto)).thenReturn(user);
            itemMapperMocked.when(() -> ItemMapper.mapToItem(itemDto)).thenReturn(item);
            bookingMapperMocked.when(() -> BookingMapper.mapToBooking(requestBookingDto, item, user)).thenReturn(booking);
            bookingMapperMocked.when(() -> BookingMapper.mapToBookingDto(booking)).thenReturn(bookingDto);

            BookingDto result = bookingService.saveBooking(1L, requestBookingDto);

            assertNotNull(result);
            verify(bookingRepository).save(any(Booking.class));
        }
    }

    @Test
    void saveBooking_unavailableItem_shouldThrow() {
        item.setAvailable(false); // недоступен

        UserDto userDto = new UserDto(1L, "User", "email@test.com");
        ItemDto itemDto = new ItemDto();

        when(userService.getUserById(1L)).thenReturn(userDto);
        when(itemService.getItemByItemId(1L, 1L)).thenReturn(itemDto);

        try (
                MockedStatic<UserMapper> userMapperMocked = mockStatic(UserMapper.class);
                MockedStatic<ItemMapper> itemMapperMocked = mockStatic(ItemMapper.class);
                MockedStatic<BookingMapper> bookingMapperMocked = mockStatic(BookingMapper.class)
        ) {
            userMapperMocked.when(() -> UserMapper.mapToUser(userDto)).thenReturn(user);
            itemMapperMocked.when(() -> ItemMapper.mapToItem(itemDto)).thenReturn(item);
            bookingMapperMocked.when(() -> BookingMapper.mapToBooking(requestBookingDto, item, user)).thenReturn(booking);

            assertThrows(ItemValidationException.class, () -> bookingService.saveBooking(1L, requestBookingDto));
        }
    }

    @Test
    void approveBooking_success_approved() {
        booking.setStatus(Status.WAITING);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any())).thenReturn(booking);

        try (MockedStatic<BookingMapper> bookingMapperMocked = mockStatic(BookingMapper.class)) {
            bookingMapperMocked.when(() -> BookingMapper.mapToBookingDto(booking)).thenReturn(new BookingDto());

            BookingDto result = bookingService.approveBooking(1L, 1L, true);

            assertNotNull(result);
            assertEquals(Status.APPROVED, booking.getStatus());
        }
    }

    @Test
    void approveBooking_success_rejected() {
        booking.setStatus(Status.WAITING);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any())).thenReturn(booking);

        try (MockedStatic<BookingMapper> bookingMapperMocked = mockStatic(BookingMapper.class)) {
            bookingMapperMocked.when(() -> BookingMapper.mapToBookingDto(booking)).thenReturn(new BookingDto());

            BookingDto result = bookingService.approveBooking(1L, 1L, false);

            assertNotNull(result);
            assertEquals(Status.REJECTED, booking.getStatus());
        }
    }

    @Test
    void approveBooking_wrongOwner_shouldThrow() {
        User anotherUser = new User();
        anotherUser.setId(2L);
        item.setOwner(anotherUser); // не тот владелец

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        assertThrows(ItemValidationException.class,
                () -> bookingService.approveBooking(1L, 1L, true));
    }

    @Test
    void getBooking_success() {
        UserDto userDto = new UserDto(1L, "User", "email@test.com");

        when(userService.getUserById(1L)).thenReturn(userDto);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        try (
                MockedStatic<UserMapper> userMapper = mockStatic(UserMapper.class);
                MockedStatic<BookingMapper> bookingMapper = mockStatic(BookingMapper.class)
        ) {
            userMapper.when(() -> UserMapper.mapToUser(userDto)).thenReturn(user);
            bookingMapper.when(() -> BookingMapper.mapToBookingDto(booking)).thenReturn(new BookingDto());

            BookingDto result = bookingService.getBooking(1L, 1L);
            assertNotNull(result);
        }
    }

    @Test
    void getAllBooking_success() {
        UserDto userDto = new UserDto(1L, "User", "email@test.com");

        when(userService.getUserById(1L)).thenReturn(userDto);
        when(bookingRepository.getAllBooking(user)).thenReturn(List.of(booking));

        try (
                MockedStatic<UserMapper> userMapper = mockStatic(UserMapper.class);
                MockedStatic<BookingMapper> bookingMapper = mockStatic(BookingMapper.class)
        ) {
            userMapper.when(() -> UserMapper.mapToUser(userDto)).thenReturn(user);
            bookingMapper.when(() -> BookingMapper.mapToBookingDto(booking)).thenReturn(new BookingDto());

            List<BookingDto> result = bookingService.getAllBooking(1L);
            assertEquals(1, result.size());
        }
    }

    @Test
    void getBookingByOwner_success() {
        UserDto userDto = new UserDto(1L, "User", "email@test.com");

        when(userService.getUserById(1L)).thenReturn(userDto);
        when(bookingRepository.getAllBooking(user)).thenReturn(List.of(booking));

        try (
                MockedStatic<UserMapper> userMapper = mockStatic(UserMapper.class);
                MockedStatic<BookingMapper> bookingMapper = mockStatic(BookingMapper.class)
        ) {
            userMapper.when(() -> UserMapper.mapToUser(userDto)).thenReturn(user);
            bookingMapper.when(() -> BookingMapper.mapToBookingDto(booking)).thenReturn(new BookingDto());

            List<BookingDto> result = bookingService.getBookingByOwner(1L);
            assertEquals(1, result.size());
        }
    }

    @Test
    void mapToBookingDto_shouldMapCorrectly() {
        // given
        User user = new User(1L, "User", "user@example.com");
        Item item = new Item(1L, "Item", "desc", true, user, null);
        Booking booking = new Booking();
        booking.setId(10L);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(Status.APPROVED);

        // when
        BookingDto dto = BookingMapper.mapToBookingDto(booking);

        // then
        assertEquals(booking.getId(), dto.getId());
        assertEquals(booking.getStart(), dto.getStart());
        assertEquals(booking.getEnd(), dto.getEnd());
        assertEquals(booking.getStatus(), dto.getStatus());
        assertNotNull(dto.getItem());
        assertNotNull(dto.getBooker());
    }

    @Test
    void mapToBooking_shouldMapCorrectly() {
        // given
        RequestBookingDto dto = new RequestBookingDto(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                5L
        );
        User user = new User(1L, "User", "user@example.com");
        Item item = new Item(5L, "Item", "desc", true, user, null);

        // when
        Booking booking = BookingMapper.mapToBooking(dto, item, user);

        // then
        assertEquals(dto.getStart(), booking.getStart());
        assertEquals(dto.getEnd(), booking.getEnd());
        assertEquals(user, booking.getBooker());
        assertEquals(item, booking.getItem());
    }
}