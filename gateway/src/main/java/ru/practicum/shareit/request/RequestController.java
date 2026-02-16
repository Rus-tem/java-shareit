package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestItemDto;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class RequestController {
    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> saveRequest(@RequestBody RequestItemDto requestItemDto,
                                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestClient.saveRequest(requestItemDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getRequestsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestClient.getRequestsByUserId(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequestsOtherUsers(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestClient.getAllRequestsOtherUsers(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@PathVariable("requestId") Long requestId,
                                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestClient.getRequestById(requestId, userId);
    }
}
