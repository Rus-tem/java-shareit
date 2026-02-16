package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestItemDto;
import ru.practicum.shareit.request.service.RequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class RequestController {
    private final RequestService requestService;

    @PostMapping
    public RequestDto saveRequest(@RequestBody RequestItemDto requestItemDto,
                                  @RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestService.saveRequest(requestItemDto, userId);
    }

    @GetMapping
    public List<RequestDto> getRequestsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestService.getRequestsByUserId(userId);
    }

    @GetMapping("/all")
    public List<RequestDto> getAllRequestsOtherUsers(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestService.getAllRequestsOtherUsers(userId);
    }

    @GetMapping("/{requestId}")
    public RequestDto getRequestById(@PathVariable("requestId") Long requestId,
                                     @RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestService.getRequestById(requestId, userId);
    }
}