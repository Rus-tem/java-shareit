package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ItemDto getItemByItemId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                   @PathVariable("itemId") Long itemId) {
        return itemService.getItemByItemId(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getAllItemsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getAllItemsByUserId(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsByText(@RequestParam(value = "text", required = false) String searchQuery, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getItemsByText(searchQuery, userId);
    }

    @PostMapping
    public ItemDto saveItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                            @RequestBody RequestItemDto requestItemDto) {
        return itemService.saveItem(userId, requestItemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable("itemId") Long itemId,
                              @RequestHeader("X-Sharer-User-Id") Long userId,
                              @RequestBody RequestItemDto requestItemDto) {
        return itemService.updateItem(itemId, requestItemDto, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto saveComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @RequestBody RequestComment requestComment,
                                  @PathVariable("itemId") Long itemId) {
        return itemService.saveComment(userId, requestComment, itemId);
    }


}
