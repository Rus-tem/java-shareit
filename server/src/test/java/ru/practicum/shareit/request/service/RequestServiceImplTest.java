package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.RequestNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestItemDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {
    @Mock
    private RequestRepository requestRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Spy
    @InjectMocks
    private RequestServiceImpl requestService;

    private User user;
    private Request request;
    private RequestItemDto requestItemDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Test User");

        request = new Request();
        request.setId(1L);
        request.setDescription("Description item");
        request.setRequester(user);

        requestItemDto = new RequestItemDto();
        requestItemDto.setDescription("Description item");
    }

    @Test
    void saveRequest() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(requestRepository.save(any(Request.class))).thenReturn(request);

        RequestDto result = requestService.saveRequest(requestItemDto, 1L);

        assertEquals("Description item", result.getDescription());
        verify(requestRepository, times(1)).save(any(Request.class));
    }

    @Test
    void getRequestsByUserId() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(requestRepository.findAll()).thenReturn(List.of(request));

        List<RequestDto> results = requestService.getRequestsByUserId(1L);

        assertEquals(1, results.size());
        assertEquals("Description item", results.get(0).getDescription());
    }

    @Test
    void getAllRequestsOtherUsers() {

        User anotherUser = new User();
        anotherUser.setId(2L);
        request.setRequester(anotherUser);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(requestRepository.findAll()).thenReturn(List.of(request));

        List<RequestDto> results = requestService.getAllRequestsOtherUsers(1L);

        assertEquals(1, results.size());
    }

    @Test
    void getRequestById() {

        when(requestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(requestRepository.findRequest(1L)).thenReturn(Collections.emptySet());

        RequestDto result = requestService.getRequestById(1L, 1L);

        assertEquals("Description item", result.getDescription());
        assertTrue(result.getItems().isEmpty());
    }

    @Test
    void saveRequest_userNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            requestService.saveRequest(requestItemDto, 99L);
        });
    }

    @Test
    void getRequestById_notFound() {
        when(requestRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RequestNotFoundException.class, () -> {
            requestService.getRequestById(999L, 1L);
        });
    }
}