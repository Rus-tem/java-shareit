package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BookingValidationException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.ItemValidationException;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.RequestComment;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserService userService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private RequestRepository requestRepository;
    @Spy
    @InjectMocks
    ItemServiceImpl itemService;

    private User user;
    private Item item;
    private RequestItemDto requestItemDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Test User");

        item = new Item();
        item.setId(1L);
        item.setName("Item1");
        item.setDescription("Desc");
        item.setAvailable(true);
        item.setOwner(user);

        requestItemDto = new RequestItemDto();
        requestItemDto.setName("Item1");
        requestItemDto.setDescription("Desc");
        requestItemDto.setAvailable(true);
    }

    @Test
    void getItemByItemId_success() {
        when(itemRepository.findAll()).thenReturn(List.of(item));
        when(commentRepository.findAll()).thenReturn(Collections.emptyList());

        ItemDto itemDto = itemService.getItemByItemId(1L, 1L);

        assertEquals("Item1", itemDto.getName());
        assertTrue(itemDto.getComments().isEmpty());
    }

    @Test
    void getItemByItemId_notFound() {
        when(itemRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(ItemNotFoundException.class,
                () -> itemService.getItemByItemId(99L, 1L));
    }

    @Test
    void getAllItemsByUserId_success() {
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<ItemDto> items = itemService.getAllItemsByUserId(1L);

        assertEquals(1, items.size());
    }

    @Test
    void getItemsByText_success() {
        when(itemRepository.getItemsByText("Item")).thenReturn(List.of(item));

        List<ItemDto> items = itemService.getItemsByText("Item", 1L);

        assertEquals(1, items.size());
        assertEquals("Item1", items.get(0).getName());
    }

    @Test
    void saveItem_success_withoutRequest() {
        UserDto userDto = new UserDto(1L, "Test User", "email@test.com");

        when(userService.getUserById(1L)).thenReturn(userDto);

        ItemDto saved = itemService.saveItem(1L, requestItemDto);

        assertEquals("Item1", saved.getName());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void saveItem_success_withRequest() {
        Request request = new Request();
        request.setId(10L);
        requestItemDto.setRequestId(10L);

        UserDto userDto = new UserDto(1L, "Test User", "test@example.com");
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");

        Item mappedItem = new Item();
        mappedItem.setName("Item1");
        mappedItem.setDescription("Desc");
        mappedItem.setAvailable(true);

        Item savedItem = new Item();
        savedItem.setId(1L);
        savedItem.setName("Item1");
        savedItem.setDescription("Desc");
        savedItem.setAvailable(true);
        savedItem.setOwner(user);
        savedItem.setRequest(request);

        ItemDto expectedItemDto = new ItemDto();

        when(userService.getUserById(1L)).thenReturn(userDto);
        when(requestRepository.findById(10L)).thenReturn(Optional.of(request));
        when(itemRepository.save(any(Item.class))).thenReturn(savedItem);

        try (
                MockedStatic<UserMapper> userMapperMockedStatic = mockStatic(UserMapper.class);
                MockedStatic<ItemMapper> itemMapperMockedStatic = mockStatic(ItemMapper.class)
        ) {
            userMapperMockedStatic.when(() -> UserMapper.mapToUser(userDto)).thenReturn(user);
            itemMapperMockedStatic.when(() -> ItemMapper.requestItemMapToItem(requestItemDto)).thenReturn(mappedItem);
            itemMapperMockedStatic.when(() -> ItemMapper.mapToItemDto(any(Item.class))).thenReturn(expectedItemDto);

            ItemDto result = itemService.saveItem(1L, requestItemDto);

            assertNotNull(result);
            assertEquals(expectedItemDto.getName(), result.getName());
            verify(itemRepository).save(any(Item.class));
        }
    }

    @Test
    void updateItem_success() {
        UserDto userDto = new UserDto(1L, "Test", "email@test.com");
        ItemDto existingItemDto = ItemMapper.mapToItemDto(item);

        when(itemRepository.findAll()).thenReturn(List.of(item));
        when(commentRepository.findAll()).thenReturn(Collections.emptyList());
        when(userService.getUserById(1L)).thenReturn(userDto);

        ItemDto result = itemService.updateItem(1L, requestItemDto, 1L);

        assertEquals("Item1", result.getName());
        verify(itemRepository).save(any(Item.class));
    }


    @Test
    void saveComment_success() {
        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setEnd(LocalDateTime.now().minusDays(1));
        booking.setStatus(Status.APPROVED);

        RequestComment commentRequest = new RequestComment("Good!");
        UserDto userDto = new UserDto(1L, "Test", "email@test.com");

        when(userService.getUserById(1L)).thenReturn(userDto);
        when(itemRepository.findAll()).thenReturn(List.of(item));
        when(commentRepository.findAll()).thenReturn(Collections.emptyList());
        when(bookingRepository.findAll()).thenReturn(List.of(booking));

        CommentDto result = itemService.saveComment(1L, commentRequest, 1L);

        assertEquals("Good!", result.getText());
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void saveComment_withoutApprovedBooking_throwsException() {
        RequestComment commentRequest = new RequestComment("Nice");
        UserDto userDto = new UserDto(1L, "Test", "email@test.com");

        when(userService.getUserById(1L)).thenReturn(userDto);
        when(itemRepository.findAll()).thenReturn(List.of(item));
        when(commentRepository.findAll()).thenReturn(Collections.emptyList());
        when(bookingRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(BookingValidationException.class,
                () -> itemService.saveComment(1L, commentRequest, 1L));
    }

    @Test
    void saveItem_validationError_missingFields() {
        RequestItemDto invalidDto = new RequestItemDto(); // пустой

        assertThrows(ItemValidationException.class,
                () -> itemService.saveItem(1L, invalidDto));
    }
}