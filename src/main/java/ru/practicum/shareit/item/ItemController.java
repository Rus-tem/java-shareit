package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public Item getItemByItemId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                @PathVariable("itemId") Long itemId) {
        return itemService.getItemByItemId(itemId, userId);
    }

    @GetMapping
    public List<Item> getAllItemsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getAllItemsByUserId(userId);
    }

    @GetMapping("/search")
    public List<Item> getItemsByText(@RequestParam(value = "text", required = false) String searchQuery, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getItemsByText(searchQuery, userId);
    }

    @PostMapping
    public Item saveItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                         @RequestBody Item item) {
        return itemService.saveItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    public Item updateItem(@PathVariable("itemId") Long itemId,
                           @RequestHeader("X-Sharer-User-Id") Long userId,
                           @RequestBody Item item) {
        return itemService.updateItem(itemId, item, userId);
    }

}
