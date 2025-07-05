package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item getItemByItemId(Long itemId, Long userId);

    List<Item> getAllItemsByUserId(Long userId);

    List<Item> getItemsByText(String searchQuery, Long userId);

    Item saveItem(Long userId, Item item);

    Item updateItem(Long itemId, Item item, Long userId);


}
