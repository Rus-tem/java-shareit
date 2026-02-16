package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.RequestUser;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        return userClient.getAllUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable("userId") Long userId) {
        return userClient.getUserById(userId);
    }

    @PostMapping
    public ResponseEntity<Object> saveUser(@RequestBody RequestUser requestUser) {
        return userClient.saveUser(requestUser);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable("userId") Long userId, @RequestBody RequestUser requestUser) {
        return userClient.updateUser(userId, requestUser);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") Long userId) {
        userClient.deleteUser(userId);
    }
}
