package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.RequestNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestItemDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private Long nextRequestId = 1L;

    @Override
    public RequestDto saveRequest(RequestItemDto requestItemDto, Long userId) {
        Request request = RequestMapper.mapToRequest(requestItemDto);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Данный пользователь не найден"));
        request.setRequester(user);
        request.setId(nextRequestId++);
        requestRepository.save(request);
        return RequestMapper.mapToRequestDto(request);
    }

    @Override
    public List<RequestDto> getRequestsByUserId(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Данный пользователь не найден"));
        return requestRepository.findAll()
                .stream()
                .filter(request -> request.getRequester().getId().equals(userId))
                .map(RequestMapper::mapToRequestDto)
                .toList();
    }

    @Override
    public List<RequestDto> getAllRequestsOtherUsers(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Данный пользователь не найден"));
        return requestRepository.findAll()
                .stream()
                .filter(request -> !request.getRequester().getId().equals(userId))
                .map(RequestMapper::mapToRequestDto)
                .toList();
    }

    @Override
    public RequestDto getRequestById(Long requestId, Long userId) {
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new RequestNotFoundException("Данный request не найден"));
        Set<ItemDto> setOfItems = requestRepository.findRequest(requestId)
                .stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toSet());
        RequestDto requestDto = RequestMapper.mapToRequestDto(request);
        requestDto.setItems(setOfItems);
        return requestDto;
    }
}
