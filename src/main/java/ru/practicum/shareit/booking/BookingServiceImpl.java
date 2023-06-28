package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.Pagination;
import ru.practicum.shareit.exeption.IncorrectParameterException;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
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
    public BookingDto save(BookingInputDto bookingInputDto, Long bookerId) {
        Booking booking = createBooking(bookingInputDto, bookerId);
        return mapper.toBookingDto(repository.save(booking));
    }

    @Override
    public BookingDto update(Long bookingId, Long userId, Boolean approved) {
        userService.findByIdUser(userId);
        Booking booking = repository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking whit id = " + bookingId + " not found in database!"));

        if (booking.getEnd().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Time of booking is finish!");
        }

        if (booking.getBooker().getId().equals(userId) && !approved) {
            booking.setStatus(CANCELED);
            log.info("User whit id = {} cancel booking whit id = {}", userId, bookingId);
        } else {

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
        }

        return mapper.toBookingDto(repository.save(booking));
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long userId) {
        userService.findByIdUser(userId);
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
    public List<BookingDto> getBookings(String state, Long userId, Integer from, Integer size) {
        userService.findByIdUser(userId);
        List<BookingDto> bookings = new ArrayList<>();
        Pageable pageable;
        Sort sortByStartDesc = Sort.by(Sort.Direction.DESC, "start");
        Page<Booking> page;
        Pagination pager = new Pagination(from, size);

        if (size == null) {
            pageable =
                    PageRequest.of(pager.getIndex(), pager.getPageSize(), sortByStartDesc);
            do {
                page = getPageBookings(state, userId, pageable);
                bookings.addAll(page.stream().map(mapper::toBookingDto).collect(toList()));
                pageable = pageable.next();
            } while (page.hasNext());

        } else {
            for (int i = pager.getIndex(); i < pager.getTotalPages(); i++) {
                pageable =
                        PageRequest.of(i, pager.getPageSize(), sortByStartDesc);
                page = getPageBookings(state, userId, pageable);
                bookings.addAll(page.stream().map(mapper::toBookingDto).collect(toList()));
                if (!page.hasNext()) {
                    break;
                }
            }
            bookings = bookings.stream().limit(size).collect(toList());
        }
        return bookings;
    }

    private Page<Booking> getPageBookings(String state, Long userId, Pageable pageable) {
        Page<Booking> page;

        switch (state) {
            case "ALL":
                page = repository.findByBookerId(userId, pageable);
                break;
            case "CURRENT":
                Sort sortByStartAsc = Sort.by(Sort.Direction.ASC, "start");
                pageable =
                        PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortByStartAsc);
                page = repository.findByBookerIdAndStartIsBeforeAndEndIsAfter(userId, LocalDateTime.now(),
                        LocalDateTime.now(), pageable);
                break;
            case "PAST":
                page = repository.findByBookerIdAndEndIsBefore(userId, LocalDateTime.now(), pageable);
                break;
            case "FUTURE":
                page = repository.findByBookerIdAndStartIsAfter(userId, LocalDateTime.now(), pageable);
                break;
            case "WAITING":
                page = repository.findByBookerIdAndStatus(userId, WAITING, pageable);
                break;
            case "REJECTED":
                page = repository.findByBookerIdAndStatus(userId, REJECTED, pageable);
                break;
            default:
                throw new IncorrectParameterException(state);
        }

        return page;
    }

    @Override
    public List<BookingDto> getBookingsOwner(String state, Long userId, Integer from, Integer size) {
        userService.findByIdUser(userId);
        List<BookingDto> bookings = new ArrayList<>();
        Pageable pageable;
        Sort sortByStartDesc = Sort.by(Sort.Direction.DESC, "start");
        Page<Booking> page;
        Pagination pager = new Pagination(from, size);

        if (size == null) {
            pageable =
                    PageRequest.of(pager.getIndex(), pager.getPageSize(), sortByStartDesc);
            do {
                page = getPageBookingsOwner(state, userId, pageable);
                bookings.addAll(page.stream().map(mapper::toBookingDto).collect(toList()));
                pageable = pageable.next();
            } while (page.hasNext());

        } else {
            for (int i = pager.getIndex(); i < pager.getTotalPages(); i++) {
                pageable =
                        PageRequest.of(i, pager.getPageSize(), sortByStartDesc);
                page = getPageBookingsOwner(state, userId, pageable);
                bookings.addAll(page.stream().map(mapper::toBookingDto).collect(toList()));
                if (!page.hasNext()) {
                    break;
                }
            }
            bookings = bookings.stream().limit(size).collect(toList());
        }
        return bookings;
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

    private Page<Booking> getPageBookingsOwner(String state, Long userId, Pageable pageable) {
        Page<Booking> page;
        switch (state) {
            case "ALL":
                page = repository.findByItem_Owner(userId, pageable);
                break;
            case "CURRENT":
                page = repository.findByItem_OwnerAndStartIsBeforeAndEndIsAfter(userId, LocalDateTime.now(),
                        LocalDateTime.now(), pageable);
                break;
            case "PAST":
                page = repository.findByItem_OwnerAndEndIsBefore(userId, LocalDateTime.now(), pageable);
                break;
            case "FUTURE":
                page = repository.findByItem_OwnerAndStartIsAfter(userId, LocalDateTime.now(),
                        pageable);
                break;
            case "WAITING":
                page = repository.findByItem_OwnerAndStatus(userId, WAITING, pageable);
                break;
            case "REJECTED":
                page = repository.findByItem_OwnerAndStatus(userId, REJECTED, pageable);
                break;
            default:
                throw new IncorrectParameterException(state);
        }

        return page;
    }

    public void validatorCreateBooking(boolean itemAvailable, LocalDateTime startBooking, LocalDateTime endBooking) {
        validatorItemAvailable(itemAvailable);
        validatorStartEndTime(startBooking, endBooking);
    }

    public Booking createBooking(BookingInputDto bookingInputDto, Long bookerId) {
        ItemDto itemCheck = itemService.findById(bookingInputDto.getItemId());
        UserDto userCheck = userService.findByIdUser(bookerId);
        validatorCreateBooking(itemCheck.getAvailable(), bookingInputDto.getStart(), bookingInputDto.getEnd());

        Booking booking = mapper.toBooking(bookingInputDto, itemCheck, userCheck);

        if (bookerId.equals(booking.getItem().getOwner())) {
            throw new NotFoundException("Owner can`t create booking his item!");
        }
        return booking;
    }
}
