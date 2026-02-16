package ru.practicum.shareit.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.shareit.item.model.Item;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemNotFoundException extends RuntimeException {
    private Item item;

    public ItemNotFoundException(String message) {
        super(message);
    }
}
