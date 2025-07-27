package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.RequestComment;
import ru.practicum.shareit.item.dto.RequestItemDto;

import java.util.List;

@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> getItemByItemId(Long itemId, Long userId) {
        return get("/" + itemId, userId);
    }

    public List<ItemDto> getAllItemsByUserId(Long userId) {
        ResponseEntity<Object> response = get("", userId);
        Object body = response.getBody();
        if (body instanceof List) {
            @SuppressWarnings("unchecked")
            List<ItemDto> itemList = (List<ItemDto>) body;
            return itemList;
        } else {
            // Обработка ошибки, если ответ не является списком
            throw new RuntimeException("Expected a List but got " + body.getClass().getName());
        }
    }

    public ResponseEntity<List<ItemDto>> getItemsByText(String searchQuery, Long userId) {
        String path = "/search?text=" + searchQuery;
        return getItemsByText(path, HttpMethod.GET, null, new ParameterizedTypeReference<List<ItemDto>>() {
        }, userId);
    }

    public ResponseEntity<Object> saveItem(Long userId, RequestItemDto requestItemDto) {
        return post("", userId, requestItemDto);
    }

    public ResponseEntity<Object> updateItem(Long itemId, RequestItemDto requestItemDto, Long userId) {
        return patch("/" + itemId, userId, requestItemDto);
    }

    public ResponseEntity<Object> saveComment(Long userId, RequestComment requestComment, Long itemId) {
        return post("/" + itemId + "/comment", userId, requestComment);
    }


}
