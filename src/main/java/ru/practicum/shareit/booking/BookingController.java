package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
public class BookingController {

    private static final String USER_ID = "X-Sharer-User-Id";

    @Autowired
    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    @ResponseBody
    @PostMapping
    public BookingDto create(@RequestBody BookingInputDto bookingInputDto,
                             @RequestHeader(USER_ID) Long bookerId) {
        log.info("Получен запрос на бронирование от пользователя с ID={}", bookerId);
        return service.create(bookingInputDto, bookerId);
    }


}
