package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.ItemValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;
    private Long nextItemId = 1L;

    //Получение Item по ItemID
    @Override
    public Item getItemByItemId(Long itemId, Long userId) {
        Optional<Item> itemOptional = itemRepository.getAllItems()
                .stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst();
        if (itemOptional.isEmpty()) {
            throw new ItemNotFoundException("Item c таким ID не найден");
        }
        return itemOptional.get();
    }

    //Получение Item по UserID
    @Override
    public List<Item> getAllItemsByUserId(Long userId) {
        return itemRepository.getAllItems()
                .stream()
                .filter(item -> item.getOwner().equals(userId))
                .toList();
    }

    // Получение Item по поисковому запросу
    @Override
    public List<Item> getItemsByText(String searchQuery, Long userId) {
        return itemRepository.getAllItems()
                .stream()
                .filter(item -> item.getDescription().toUpperCase().equals(searchQuery) || item.getName().toUpperCase().equals(searchQuery))
                .filter(item -> item.getAvailable().equals("true"))
                .toList();
    }

    //Сохранение Item
    @Override
    public Item saveItem(Long userId, Item item) {
        checkItem(item);
        userService.getUserById(userId);
        item.setId(nextItemId++);
        item.setOwner(userId);
        return itemRepository.saveItem(userId, item);
    }

    // Обновление Item
    @Override
    public Item updateItem(Long itemId, Item newItem, Long userId) {
        Item oldItem = getItemByItemId(itemId, userId);
        if (oldItem.getOwner() != userId) {
            throw new ItemNotFoundException("Не корректное значение userId");
        }
        if (newItem.getAvailable() == null || newItem.getAvailable().isBlank()) {
            newItem.setAvailable(oldItem.getAvailable());
        }
        if (newItem.getDescription() == null || newItem.getDescription().isBlank()) {
            newItem.setDescription(oldItem.getDescription());
        }
        if (newItem.getName() == null || newItem.getName().isBlank()) {
            newItem.setName(oldItem.getName());
        }
        return itemRepository.updateItem(oldItem, newItem);
    }

    // Метод для проверки Item
    private void checkItem(Item item) {
        if (item.getAvailable() == null || item.getAvailable().isBlank()) {
            throw new ItemValidationException("Не корректное поле available", item);
        }
        if (item.getName() == null || item.getName().isBlank()) {
            throw new ItemValidationException("Не корректное поле name", item);
        }
        if (item.getDescription() == null || item.getDescription().isBlank()) {
            throw new ItemValidationException("Не корректное поле description", item);
        }
    }
}
