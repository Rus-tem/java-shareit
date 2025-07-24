package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
public class ShareItItemTests {
    @Mock
    private ItemService itemService;
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private ItemController itemController;


    @Test
    void getAllItem() {
        List<Item> expectedList = List.of(new Item());
        Mockito.when(itemRepository.findAll()).thenReturn(expectedList);
        List<Item> actualList = itemRepository.findAll();
        assertEquals(expectedList, actualList);
    }

    @Test
    void getItemById() {
        Long itemId = 0L;
        Item expectedItem = new Item();
        itemRepository.save(expectedItem);
        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(expectedItem));
        Item actualItem = itemRepository.findById(itemId).get();
        assertEquals(expectedItem, actualItem);
    }

    @Test
    void saveItem() {
        Long itemId = 0L;
        Item itemToSave = new Item();
        itemRepository.save(itemToSave);
        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(itemToSave));
        Item actualItem = itemRepository.findById(itemId).get();
        assertEquals(itemToSave.getId(), actualItem.getId());
    }
}
