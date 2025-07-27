package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BookingValidationException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.ItemValidationException;
import ru.practicum.shareit.exception.RequestNotFoundException;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.RequestComment;
import ru.practicum.shareit.item.comment.mapper.CommentMapper;
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
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final RequestRepository requestRepository;
    private Long nextItemId = 1L;
    private Long nextCommentId = 1L;

    //Получение Item по ItemID
    @Override
    public ItemDto getItemByItemId(Long itemId, Long userId) {
        Optional<Item> itemOptional = itemRepository.findAll()
                .stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst();
        if (itemOptional.isEmpty()) {
            throw new ItemNotFoundException("Item c таким ID не найден");
        }
        ItemDto itemDto = ItemMapper.mapToItemDto(itemOptional.get());
        List<CommentDto> commentDtoList = commentRepository.findAll()
                .stream()
                .filter(comment -> comment.getItem().getId().equals(itemId))
                .map(CommentMapper::mapToCommentDto)
                .toList();
        itemDto.setComments(commentDtoList);
        return itemDto;
    }

    //Получение Item по UserID
    @Override
    public List<ItemDto> getAllItemsByUserId(Long userId) {
        return itemRepository.findAll()
                .stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    // Получение Item по поисковому запросу
    @Override
    public List<ItemDto> getItemsByText(String searchQuery, Long userId) {
        return itemRepository.getItemsByText(searchQuery).stream()
                .map(ItemMapper::mapToItemDto).toList();
    }

    //Сохранение Item
    @Override
    public ItemDto saveItem(Long userId, RequestItemDto requestItemDto) {
        Item item = ItemMapper.requestItemMapToItem(requestItemDto);
        if (requestItemDto.getRequestId() != null) {
            Request request = requestRepository.findById(requestItemDto.getRequestId()).orElseThrow(() -> new RequestNotFoundException("Данный request не найден"));
            item.setRequest(request);
        }
        checkItem(item);
        UserDto userDto = userService.getUserById(userId);
        item.setId(nextItemId++);
        item.setOwner(UserMapper.mapToUser(userDto));
        itemRepository.save(item);
        return ItemMapper.mapToItemDto(item);
    }

    // Обновление Item
    @Override
    public ItemDto updateItem(Long itemId, RequestItemDto requestItemDto, Long userId) {
        Item newItem = ItemMapper.requestItemMapToItem(requestItemDto);
        ItemDto oldItem = getItemByItemId(itemId, userId);
        User user = UserMapper.mapToUser(userService.getUserById(userId));
        if (!oldItem.getOwner().getId().equals(user.getId())) {
            throw new ItemNotFoundException("Не корректное значение userId");
        }
        if (newItem.getAvailable() == null) {
            newItem.setAvailable(oldItem.getAvailable());
        }
        if (newItem.getDescription() == null || newItem.getDescription().isBlank()) {
            newItem.setDescription(oldItem.getDescription());
        }
        if (newItem.getName() == null || newItem.getName().isBlank()) {
            newItem.setName(oldItem.getName());
        }
        newItem.setId(itemId);
        newItem.setOwner(user);
        itemRepository.save(newItem);
        return ItemMapper.mapToItemDto(newItem);
    }

    // Сохранение комментария
    @Override
    public CommentDto saveComment(Long userId, RequestComment requestComment, Long itemId) {

        User user = UserMapper.mapToUser(userService.getUserById(userId));
        Item item = ItemMapper.mapToItem(getItemByItemId(itemId, userId));

        List<Booking> bookingList = bookingRepository.findAll()
                .stream()
                .filter(booking -> booking.getBooker().getId().equals(userId) &&
                                   booking.getEnd().isBefore(LocalDateTime.now()) &&
                                   booking.getStatus().equals(Status.APPROVED))
                .toList();

        if (bookingList.isEmpty()) {
            throw new BookingValidationException("Данный booking не завершен");
        }

        Comment comment = new Comment();
        comment.setId(nextCommentId++);
        comment.setText(requestComment.getText());
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        commentRepository.save(comment);
        return CommentMapper.mapToCommentDto(comment);
    }

    // Метод для проверки Item
    private void checkItem(Item item) {
        if (item.getAvailable() == null) {
            throw new ItemValidationException("Не корректное поле available");
        }
        if (item.getName() == null || item.getName().isBlank()) {
            throw new ItemValidationException("Не корректное поле name");
        }
        if (item.getDescription() == null || item.getDescription().isBlank()) {
            throw new ItemValidationException("Не корректное поле description");
        }
    }
}
