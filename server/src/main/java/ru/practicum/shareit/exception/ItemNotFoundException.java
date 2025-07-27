package ru.practicum.shareit.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.shareit.item.model.Item;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter
public class ItemNotFoundException extends RuntimeException {
    private Item item;

    public ItemNotFoundException(String message) {
        super(message);
    }
}
