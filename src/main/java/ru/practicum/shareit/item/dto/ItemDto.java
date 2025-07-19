package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long lastBooking;
    private Long nextBooking;
    private List<CommentDto> comments;
    private User owner_id;
    private Long request_id;
}
