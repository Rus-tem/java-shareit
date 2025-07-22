package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.RequestComment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.RequestItemDto;

import java.util.List;

public interface ItemService {

    ItemDto getItemByItemId(Long itemId, Long userId);

    List<ItemDto> getAllItemsByUserId(Long userId);

    List<ItemDto> getItemsByText(String searchQuery, Long userId);

    ItemDto saveItem(Long userId, RequestItemDto requestItemDto);

    ItemDto updateItem(Long itemId, RequestItemDto requestItemDto, Long userId);

    CommentDto saveComment(Long userId, RequestComment requestComment, Long itemId);


}
