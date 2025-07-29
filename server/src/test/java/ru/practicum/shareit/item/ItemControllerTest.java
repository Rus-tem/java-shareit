package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.RequestComment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getItemByItemId_shouldReturnItemDto() throws Exception {
        long userId = 1L;
        long itemId = 2L;

        ItemDto itemDto = new ItemDto();
        itemDto.setId(itemId);
        itemDto.setName("Drill");
        itemDto.setDescription("Powerful drill");
        itemDto.setAvailable(true);

        when(itemService.getItemByItemId(itemId, userId)).thenReturn(itemDto);

        mockMvc.perform(get("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemId))
                .andExpect(jsonPath("$.name").value("Drill"))
                .andExpect(jsonPath("$.available").value(true));

        verify(itemService).getItemByItemId(itemId, userId);
    }

    @Test
    void getAllItemsByUserId_shouldReturnList() throws Exception {
        long userId = 1L;

        ItemDto itemDto = new ItemDto(1L, "Drill", "Power drill", true,
                null, null, List.of(), null, null);

        when(itemService.getAllItemsByUserId(userId)).thenReturn(List.of(itemDto));

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Drill"));

        verify(itemService).getAllItemsByUserId(userId);
    }

    @Test
    void getItemsByText_shouldReturnList() throws Exception {
        long userId = 1L;
        String text = "drill";

        ItemDto itemDto = new ItemDto(1L, "Drill", "Power drill", true,
                null, null, List.of(), null, null);

        when(itemService.getItemsByText(text, userId)).thenReturn(List.of(itemDto));

        mockMvc.perform(get("/items/search")
                        .param("text", text)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Power drill"));

        verify(itemService).getItemsByText(text, userId);
    }

    @Test
    void saveItem_shouldReturnSavedItem() throws Exception {
        long userId = 1L;

        RequestItemDto requestItemDto = new RequestItemDto();
        requestItemDto.setName("Drill");
        requestItemDto.setDescription("Power drill");
        requestItemDto.setAvailable(true);

        ItemDto savedItem = new ItemDto(1L, "Drill", "Power drill", true,
                null, null, List.of(), null, null);

        when(itemService.saveItem(eq(userId), any(RequestItemDto.class))).thenReturn(savedItem);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestItemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Drill"));

        verify(itemService).saveItem(eq(userId), any(RequestItemDto.class));
    }

    @Test
    void updateItem_shouldReturnUpdatedItem() throws Exception {
        long userId = 1L;
        long itemId = 2L;

        RequestItemDto requestItemDto = new RequestItemDto();
        requestItemDto.setName("Updated Drill");
        requestItemDto.setDescription("Updated description");
        requestItemDto.setAvailable(false);

        ItemDto updatedItem = new ItemDto(itemId, "Updated Drill", "Updated description",
                false, null, null, List.of(), null, null);

        when(itemService.updateItem(eq(itemId), any(RequestItemDto.class), eq(userId))).thenReturn(updatedItem);

        mockMvc.perform(patch("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestItemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Drill"))
                .andExpect(jsonPath("$.available").value(false));

        verify(itemService).updateItem(eq(itemId), any(RequestItemDto.class), eq(userId));
    }

    @Test
    void saveComment_shouldReturnCommentDto() throws Exception {
        long userId = 1L;
        long itemId = 2L;

        RequestComment requestComment = new RequestComment("Great item!");

        CommentDto commentDto = new CommentDto();
        commentDto.setId(10L);
        commentDto.setText("Great item!");
        commentDto.setAuthorName("John");
        commentDto.setCreated(LocalDateTime.now());

        when(itemService.saveComment(eq(userId), any(RequestComment.class), eq(itemId))).thenReturn(commentDto);

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestComment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.text").value("Great item!"))
                .andExpect(jsonPath("$.authorName").value("John"));

        verify(itemService).saveComment(eq(userId), any(RequestComment.class), eq(itemId));
    }
}