package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;

import java.util.List;

import static ru.practicum.shareit.Constant.USER_ID;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
public class BookingController {

    //private static final String USER_ID = "X-Sharer-User-Id";

    @Autowired
    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    @ResponseBody
    @PostMapping
    public BookingDto create(@RequestBody BookingInputDto bookingInputDto,
                             @RequestHeader(USER_ID) Long bookerId) {
        log.info("Request Post received to add booking from user whit id = {}", bookerId);
        return service.save(bookingInputDto, bookerId);
    }

    @ResponseBody
    @PatchMapping("/{bookingId}")
    public BookingDto update(@PathVariable Long bookingId,
                             @RequestHeader(USER_ID) Long userId, @RequestParam Boolean approved) {
        log.info("Request Patch received to update booking whit с id = {}", bookingId);
        return service.update(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@PathVariable Long bookingId, @RequestHeader(USER_ID) Long userId) {
        log.info("Request Get received to find booking by id = {}", bookingId);
        return service.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getBookings(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                        @RequestHeader(USER_ID) Long userId,
                                        @RequestParam(defaultValue = "0") Integer from,
                                        @RequestParam(required = false) Integer size) {
        log.info("Request Get received whit parameter STATE = {}" +
                " to find list booking user`s whit id = {} ", state, userId);
        return service.getBookings(state, userId, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsOwner(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                             @RequestHeader(USER_ID) Long userId,
                                             @RequestParam(defaultValue = "0") Integer from,
                                             @RequestParam(required = false) Integer size) {
        log.info("Request Get received whit parameter STATE = {}" +
                " to find list all bookings items the owner whit id = {}", userId, state);
        return service.getBookingsOwner(state, userId, from, size);
    }
}
