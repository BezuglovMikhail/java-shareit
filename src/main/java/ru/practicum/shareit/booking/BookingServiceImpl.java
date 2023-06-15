package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.IncorrectParameterException;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.BookingStatus.*;
import static ru.practicum.shareit.validator.Validator.*;

@Slf4j
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository repository;

    private UserService userService;
    private ItemService itemService;
    private final BookingMapper mapper;

    @Autowired
    @Lazy
    public BookingServiceImpl(BookingRepository bookingRepository,
                              UserService userService,
                              ItemService itemService,
                              BookingMapper bookingMapper) {
        this.repository = bookingRepository;
        this.userService = userService;
        this.itemService = itemService;
        this.mapper = bookingMapper;
    }

    @Override
    public BookingDto create(BookingInputDto bookingInputDto, Long bookerId) {
        Optional<Item> itemCheck = itemService.findItemById(bookingInputDto.getItemId());
        Optional<User> userCheck = userService.findUserById(bookerId);
        validatorUserId(userCheck, bookerId);
        validatorItemId(itemCheck, bookingInputDto.getItemId());
        validatorItemAvailable(itemCheck.get().getAvailable());
        validatorStartEndTime(bookingInputDto.getStart(), bookingInputDto.getEnd());
        Booking booking = mapper.toBooking(bookingInputDto, itemCheck.get(), userCheck.get());

        if (bookerId.equals(booking.getItem().getOwner())) {
            throw new NotFoundException("item with id = " + bookingInputDto.getItemId() +
                    " not found in database!");
        }
        return mapper.toBookingDto(repository.save(booking));
    }

    @Override
    public BookingDto update(Long bookingId, Long userId, Boolean approved) {
        validatorUserId(userService.findUserById(userId), userId);
        Booking booking = repository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking whit id = " + bookingId + " not found in database!"));
        if (booking.getEnd().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Time of booking is finish!");
        }
        if (booking.getBooker().getId().equals(userId)) {
            if (!approved) {
                booking.setStatus(CANCELED);
                log.info("User whit id = {} cancel booking whit id = {}", userId, bookingId);
            } else {
                throw new NotFoundException("Only the owner of the item can APPROVED the booking!");
            }
        }

        validatorItemOwner(booking.getItem().getOwner(), userId);

        if (!booking.getStatus().equals(WAITING) &&
                !booking.getStatus().equals(CANCELED)) {
            throw new IncorrectParameterException("BookingStatus");
        }
        if (approved) {
            booking.setStatus(APPROVED);
            log.info("Owner whit id = {} APPROVED booking whit id = {}", userId, bookingId);
        } else {
            booking.setStatus(REJECTED);
            log.info("Owner whit id = {} REJECTED booking whit id = {}", userId, bookingId);
        }
        if (booking.getStatus().equals(CANCELED)) {
            throw new ValidationException("Booking was cancel!");
        }
        return mapper.toBookingDto(repository.save(booking));
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long userId) {
        validatorUserId(userService.findUserById(userId), userId);
        Booking booking = repository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking whit id = " + bookingId + " not found in database!"));
        if (booking.getBooker().getId().equals(userId) || validatorItemOwner(booking.getItem().getOwner(), userId)) {
            return mapper.toBookingDto(booking);
        } else {
            throw new NotFoundException("Only owner or booker can view information of booking." +
                    " You can`t view information of booking!");
        }
    }

    @Override
    public List<BookingDto> getBookings(String state, Long userId) {
        validatorUserId(userService.findUserById(userId), userId);
        List<Booking> bookings;
        Sort sortByStartDesc = Sort.by(Sort.Direction.DESC, "start");
        Sort sortByStartAsc = Sort.by(Sort.Direction.ASC, "start");
        switch (state) {
            case "ALL":
                bookings = repository.findByBookerId(userId, sortByStartDesc);
                break;
            case "CURRENT":
                bookings = repository.findByBookerIdAndStartIsBeforeAndEndIsAfter(userId, LocalDateTime.now(),
                        LocalDateTime.now(), sortByStartAsc);
                break;
            case "PAST":
                bookings = repository.findByBookerIdAndEndIsBefore(userId, LocalDateTime.now(), sortByStartDesc);
                break;
            case "FUTURE":
                bookings = repository.findByBookerIdAndStartIsAfter(userId, LocalDateTime.now(), sortByStartDesc);
                break;
            case "WAITING":
                bookings = repository.findByBookerIdAndStatus(userId, WAITING, sortByStartDesc);
                break;
            case "REJECTED":
                bookings = repository.findByBookerIdAndStatus(userId, REJECTED, sortByStartDesc);
                break;
            default:
                throw new IncorrectParameterException(state);
        }
        return bookings.stream()
                .map(mapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getBookingsOwner(String state, Long userId) {
        validatorUserId(userService.findUserById(userId), userId);
        List<Booking> bookings;
        Sort sortByStartDesc = Sort.by(Sort.Direction.DESC, "start");
        switch (state) {
            case "ALL":
                bookings = repository.findByItem_Owner(userId, sortByStartDesc);
                break;
            case "CURRENT":
                bookings = repository.findByItem_OwnerAndStartIsBeforeAndEndIsAfter(userId, LocalDateTime.now(),
                        LocalDateTime.now(), sortByStartDesc);
                break;
            case "PAST":
                bookings = repository.findByItem_OwnerAndEndIsBefore(userId, LocalDateTime.now(), sortByStartDesc);
                break;
            case "FUTURE":
                bookings = repository.findByItem_OwnerAndStartIsAfter(userId, LocalDateTime.now(),
                        sortByStartDesc);
                break;
            case "WAITING":
                bookings = repository.findByItem_OwnerAndStatus(userId, WAITING, sortByStartDesc);
                break;
            case "REJECTED":
                bookings = repository.findByItem_OwnerAndStatus(userId, REJECTED, sortByStartDesc);
                break;
            default:
                throw new IncorrectParameterException(state);
        }
        return bookings.stream()
                .map(mapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookingShortDto getLastBooking(Long itemId) {
        BookingShortDto bookingShortDto =
                mapper.toBookingShortDto(
                        repository.findFirstByItem_IdAndStartBeforeAndStatusOrderByEndDesc(
                                itemId,
                                LocalDateTime.now(),
                                APPROVED)
                );
        return bookingShortDto;
    }

    @Override
    public BookingShortDto getNextBooking(Long itemId) {
        BookingShortDto bookingShortDto =
                mapper.toBookingShortDto(repository.findFirstByItem_IdAndStartAfterAndStatusOrderByStartAsc(itemId,
                        LocalDateTime.now(), APPROVED));
        return bookingShortDto;
    }

    @Override
    public Booking getBookingWithUserBookedItem(Long itemId, Long userId) {
        return repository.findFirstByItem_IdAndBooker_IdAndEndIsBeforeAndStatus(itemId,
                userId, LocalDateTime.now(), APPROVED);
    }
}
