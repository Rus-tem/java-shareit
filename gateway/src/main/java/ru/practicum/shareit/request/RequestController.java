package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestItemDto;

import java.util.List;

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
    public List<RequestDto> getRequestsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestClient.getRequestsByUserId(userId);
    }

    @GetMapping("/all")
    public List<RequestDto> getAllRequestsOtherUsers(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestClient.getAllRequestsOtherUsers(userId);
    }

    @GetMapping("/{requestId}")
    public Object getRequestById(@PathVariable("requestId") Long requestId,
                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestClient.getRequestById(requestId, userId);
    }
}
