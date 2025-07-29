package ru.practicum.shareit.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemValidationException extends RuntimeException {
    private Item item;

    public ItemValidationException(String message) {
        super(message);
    }
}
