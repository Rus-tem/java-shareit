package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.RequestUser;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Service
public class UserClient extends BaseClient {

    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> getUserById(Long userId) {
        return get("/" + userId);
    }

    public List<UserDto> getAllUsers() {
        ResponseEntity<Object> response = get("");
        Object body = response.getBody();
        if (body instanceof List) {
            @SuppressWarnings("unchecked")
            List<UserDto> userList = (List<UserDto>) body;
            return userList;
        } else {
            // Обработка ошибки, если ответ не является списком
            throw new RuntimeException("Expected a List but got " + body.getClass().getName());
        }
    }

    public ResponseEntity<Object> saveUser(RequestUser requestUser) {
        return post("", requestUser);
    }

    public ResponseEntity<Object> updateUser(Long userId, RequestUser requestUser) {
        return patch("/" + userId, requestUser);
    }

    public void deleteUser(Long userId) {
        delete("/" + userId);
    }


}
