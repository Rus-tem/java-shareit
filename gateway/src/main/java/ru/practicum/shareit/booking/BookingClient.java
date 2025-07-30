package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.client.BaseClient;

import java.util.List;
import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";


    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> getBooking(Long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }

    public List<BookingDto> getBookingByOwner(Long userId) {
        ResponseEntity<Object> response = get("", userId);
        Object body = response.getBody();
        if (body instanceof List) {
            @SuppressWarnings("unchecked")
            List<BookingDto> bookingDtoList = (List<BookingDto>) body;
            return bookingDtoList;
        } else {
            // Обработка ошибки, если ответ не является списком
            throw new RuntimeException("Expected a List but got " + body.getClass().getName());
        }
    }

    public List<BookingDto> getAllBooking(Long userId) {
        ResponseEntity<Object> response = get("", userId);
        Object body = response.getBody();
        if (body instanceof List) {
            @SuppressWarnings("unchecked")
            List<BookingDto> bookingDtoList = (List<BookingDto>) body;
            return bookingDtoList;
        } else {
            // Обработка ошибки, если ответ не является списком
            throw new RuntimeException("Expected a List but got " + body.getClass().getName());
        }
    }

    public ResponseEntity<Object> saveBooking(Long userId, RequestBookingDto requestBookingDto) {
        return post("", userId, requestBookingDto);
    }

    public ResponseEntity<Object> approveBooking(Long bookingId, Long userId, Boolean searchQuery) {
        Map<String, Object> params = Map.of("approved", searchQuery);
        return patch("/" + bookingId, userId, params, null);
    }


}
