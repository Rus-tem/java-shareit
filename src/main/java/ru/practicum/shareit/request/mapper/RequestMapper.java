package ru.practicum.shareit.request.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestItemDto;
import ru.practicum.shareit.request.model.Request;

@NoArgsConstructor(access = AccessLevel.PRIVATE)

public class RequestMapper {
    public static Request mapToRequest(RequestItemDto requestItemDto) {
        Request request = new Request();
        request.setDescription(requestItemDto.getDescription());
        request.setCreated(requestItemDto.getCreated());
        return request;
    }

    public static RequestDto mapToRequestDto(Request request) {
        RequestDto requestDto = new RequestDto();
        requestDto.setId(request.getId());
        requestDto.setRequester(request.getRequester());
        requestDto.setDescription(request.getDescription());
        requestDto.setCreated(request.getCreated());
        return requestDto;
    }

    public static Request mapRequestDtoToRequest(RequestDto requestDto) {
        Request request = new Request();
        request.setId(requestDto.getId());
        request.setRequester(requestDto.getRequester());
        request.setDescription(requestDto.getDescription());
        request.setCreated(requestDto.getCreated());
        return request;
    }
}
