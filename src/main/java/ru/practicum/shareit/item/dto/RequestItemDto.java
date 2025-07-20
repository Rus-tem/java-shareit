package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

@Data
public class RequestItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private User owner_id;
    private Long request_id;
}
