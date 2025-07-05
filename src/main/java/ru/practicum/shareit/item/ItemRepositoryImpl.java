package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final List<Item> listOfItems = new ArrayList<>();

    @Override
    public List<Item> getAllItems() {
        return listOfItems;
    }

    @Override
    public Item saveItem(Long userId, Item item) {
        listOfItems.add(item);
        return item;
    }

    @Override
    public Item updateItem(Item oldItem, Item newItem) {
        Item item = new Item(oldItem.getId(), newItem.getName(), newItem.getDescription(), newItem.getAvailable(), oldItem.getOwner(), oldItem.getRequest());
        listOfItems.remove(oldItem);
        listOfItems.add(item);
        return item;
    }
}
