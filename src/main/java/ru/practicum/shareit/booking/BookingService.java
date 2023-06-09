package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;

public interface BookingService {
    BookingDto create(BookingInputDto bookingDto, Long bookerId);
}
