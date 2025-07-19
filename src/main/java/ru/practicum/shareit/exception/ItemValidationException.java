package ru.practicum.shareit.exception;

import lombok.Getter;
import ru.practicum.shareit.item.model.Item;

@Getter
public class ItemValidationException extends RuntimeException {
    Item item;

    public ItemValidationException(String message) {
        super(message);
    }
}
