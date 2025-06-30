package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    private Long id;
    private String start;
    private String end;
    private String item;
    private String booker;
    private Enum<Status> status;
}