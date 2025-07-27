package ru.practicum.shareit.item.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {

    public static ItemDto mapToItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        if (item.getOwner() != null) {
            itemDto.setOwner(UserMapper.mapToUserDto(item.getOwner()));
        }
        if (item.getRequest() != null) {
            itemDto.setRequest(RequestMapper.mapToRequestDto(item.getRequest()));
        }
        return itemDto;
    }

    public static Item requestItemMapToItem(RequestItemDto requestItemDto) {
        Item item = new Item();
        item.setId(requestItemDto.getId());
        item.setName(requestItemDto.getName());
        item.setDescription(requestItemDto.getDescription());
        item.setAvailable(requestItemDto.getAvailable());
        if (requestItemDto.getOwner() != null) {
            item.setOwner(UserMapper.mapToUser(requestItemDto.getOwner()));
        }
        return item;
    }

    public static Item mapToItem(ItemDto itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        if (itemDto.getOwner() != null) {
            item.setOwner(UserMapper.mapToUser(itemDto.getOwner()));
        }
        if (item.getRequest() != null) {
            item.setRequest(RequestMapper.mapRequestDtoToRequest(itemDto.getRequest()));
        }
        return item;
    }
}