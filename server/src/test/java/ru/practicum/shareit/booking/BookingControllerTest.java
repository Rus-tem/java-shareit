package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    private final Long userId = 1L;
    private final Long bookingId = 1L;

    private BookingDto createSampleBookingDto() {
        return new BookingDto(
                bookingId,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                new ItemDto(),
                new UserDto(userId, "user@example.com", "User Name"),
                Status.WAITING
        );
    }

    @Test
    void getBooking_shouldReturnBookingDto() throws Exception {
        BookingDto bookingDto = createSampleBookingDto();
        when(bookingService.getBooking(userId, bookingId)).thenReturn(bookingDto);

        mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId().intValue())))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().name())));
    }

    @Test
    void getAllBooking_shouldReturnList() throws Exception {
        when(bookingService.getAllBooking(userId)).thenReturn(List.of(createSampleBookingDto()));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));
    }

    @Test
    void getBookingByOwner_shouldReturnList() throws Exception {
        when(bookingService.getBookingByOwner(userId)).thenReturn(List.of(createSampleBookingDto()));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));
    }

    @Test
    void saveBooking_shouldReturnSavedBooking() throws Exception {
        RequestBookingDto request = new RequestBookingDto();
        request.setStart(LocalDateTime.now().plusDays(1));
        request.setEnd(LocalDateTime.now().plusDays(2));
        request.setItemId(10L);

        BookingDto bookingDto = createSampleBookingDto();

        when(bookingService.saveBooking(userId, request)).thenReturn(bookingDto);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId().intValue())));
    }

    @Test
    void approveBooking_shouldReturnApprovedBooking() throws Exception {
        BookingDto bookingDto = createSampleBookingDto();
        bookingDto.setStatus(Status.APPROVED);

        when(bookingService.approveBooking(bookingId, userId, true)).thenReturn(bookingDto);

        mockMvc.perform(patch("/bookings/{bookingId}?approved=true", bookingId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("APPROVED")));
    }
}