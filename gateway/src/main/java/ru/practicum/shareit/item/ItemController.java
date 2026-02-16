package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.RequestComment;
import ru.practicum.shareit.item.dto.RequestItemDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemByItemId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @PathVariable("itemId") Long itemId) {
        return itemClient.getItemByItemId(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.getAllItemsByUserId(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemsByText(@RequestParam(value = "text", required = false) String searchQuery, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.getItemsByText(searchQuery, userId);
    }

    @PostMapping
    public ResponseEntity<Object> saveItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @RequestBody RequestItemDto requestItemDto) {
        return itemClient.saveItem(userId, requestItemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@PathVariable("itemId") Long itemId,
                                             @RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestBody RequestItemDto requestItemDto) {
        return itemClient.updateItem(itemId, requestItemDto, userId);
    }


    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> saveComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestBody RequestComment requestComment,
                                              @PathVariable("itemId") Long itemId) {
        return itemClient.saveComment(userId, requestComment, itemId);
    }


}
