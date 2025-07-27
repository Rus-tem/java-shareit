package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestItemDto;

import java.util.List;

public interface RequestService {
    RequestDto saveRequest(RequestItemDto requestItemDto, Long userId);

    List<RequestDto> getRequestsByUserId(Long userId);

    List<RequestDto> getAllRequestsOtherUsers(Long userId);

    RequestDto getRequestById(Long requestId, Long userId);
}
