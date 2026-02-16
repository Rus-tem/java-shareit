package ru.practicum.shareit.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TestExceptionController.class)
@Import(CommonExceptionHandler.class)
class CommonExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    // ---------- USER EXCEPTIONS ----------

    @Test
    @DisplayName("Должен вернуть 400 при UserValidationException")
    void userValidationException_shouldReturn400() throws Exception {
        mockMvc.perform(get("/test/user-validation"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.['Invalid user data'].id").value(1));
    }

    @Test
    @DisplayName("Должен вернуть 409 при UserConflictException")
    void userConflictException_shouldReturn409() throws Exception {
        mockMvc.perform(get("/test/user-conflict"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.['User conflict occurred'].id").value(1));
    }

    @Test
    @DisplayName("Должен вернуть 404 при UserNotFoundException")
    void userNotFoundException_shouldReturn404() throws Exception {
        mockMvc.perform(get("/test/user-not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.['User not found']").doesNotExist());
    }

    // ---------- ITEM EXCEPTIONS ----------

    @Test
    @DisplayName("Должен вернуть 400 при ItemValidationException")
    void itemValidationException_shouldReturn400() throws Exception {
        mockMvc.perform(get("/test/item-validation"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid Item"));

    }

    @Test
    @DisplayName("Должен вернуть 404 при ItemNotFoundException")
    void itemNotFoundException_shouldReturn404() throws Exception {
        mockMvc.perform(get("/test/item-not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.['Item not found']").doesNotExist());
    }

    // ---------- BOOKING EXCEPTIONS ----------

    @Test
    @DisplayName("Должен вернуть 400 при BookingValidationException")
    void bookingValidationException_shouldReturn400() throws Exception {
        mockMvc.perform(get("/test/booking-validation"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid booking"));
    }

    @Test
    @DisplayName("Должен вернуть 500 при NullFoundException")
    void nullFoundException_shouldReturn500() throws Exception {
        mockMvc.perform(get("/test/null-found"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.['Null value encountered']").doesNotExist());
    }

    // ---------- REQUEST EXCEPTIONS ----------

    @Test
    @DisplayName("Должен вернуть 404 при RequestNotFoundException")
    void requestNotFoundException_shouldReturn404() throws Exception {
        mockMvc.perform(get("/test/request-not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.['Request not found']").doesNotExist());
    }
}

