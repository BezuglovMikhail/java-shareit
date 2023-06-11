package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;

import javax.validation.Valid;
import java.util.List;

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
        log.info("Получен запрос на бронирование от пользователя с id = {}", bookerId);
        return service.create(bookingInputDto, bookerId);
    }

    @ResponseBody
    @PatchMapping("/{bookingId}")
    public BookingDto update(@PathVariable Long bookingId,
                             @RequestHeader(USER_ID) Long userId, @RequestParam Boolean approved) {
        log.info("Получен запрос на обновление статуса бронирования с id = {}", bookingId);
        return service.update(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@PathVariable Long bookingId, @RequestHeader(USER_ID) Long userId) {
        log.info("Получен запрос на получение на получение информации о бронировании с id = {}", bookingId);
        return service.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getBookings(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                        @RequestHeader(USER_ID) Long userId) {
        log.info("Получен запрос на получение списка всех бронирований" +
                " пользователя с id = {} с параметром STATE = {}", userId, state);
        return service.getBookings(state, userId);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsOwner(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                             @RequestHeader(USER_ID) Long userId) {
        log.info("Получен запрос на получение списка всех бронирований вещей" +
                " владельца с id = {} с параметром STATE = {}", userId, state);
        return service.getBookingsOwner(state, userId);
    }
}
