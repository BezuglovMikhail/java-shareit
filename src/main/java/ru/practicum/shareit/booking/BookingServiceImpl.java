package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.IncorrectParameterException;
import ru.practicum.shareit.NotFoundException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.exeption.ValidationException;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.BookingStatus.*;

@Slf4j
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository repository;

    private final UserRepository userRepository;

    private final ItemRepository itemRepository;
    private final BookingMapper mapper;

    @Autowired
    @Lazy
    public BookingServiceImpl(BookingRepository bookingRepository, UserRepository userRepository, ItemRepository itemRepository, BookingMapper bookingMapper) {
        this.repository = bookingRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.mapper = bookingMapper;
    }

    @Override
    public BookingDto create(BookingInputDto bookingInputDto, Long bookerId) {
        validatorUserId(bookerId);
        validatorItemId(bookingInputDto.getItemId());
        validatorItemAvailable(bookingInputDto.getItemId());
        validatorStartEndTime(bookingInputDto.getStart(), bookingInputDto.getEnd());
        Booking booking = mapper.toBooking(bookingInputDto, bookerId);

        if (bookerId.equals(booking.getItem().getOwner())) {
            throw new NotFoundException("Вещь с id =" + bookingInputDto.getItemId() +
                    " не может быть забронирована владельцем!");
        }
        return mapper.toBookingDto(repository.save(booking));
    }

    @Override
    public BookingDto update(Long bookingId, Long userId, Boolean approved) {
        validatorUserId(userId);
        Booking booking = repository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с ID=" + bookingId + " не найдено!"));
        if (booking.getEnd().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Время бронирования уже истекло!");
        }
        if (booking.getBooker().getId().equals(userId)) {
            if (!approved) {
                booking.setStatus(CANCELED);
                log.info("Пользователь с ID={} отменил бронирование с ID={}", userId, bookingId);
            } else {
                throw new NotFoundException("Подтвердить бронирование может только владелец вещи!");
            }
        }
        //if ((checker.isItemOwner(booking.getItem().getId(), userId)) &&
        //     (!booking.getStatus().equals(Status.CANCELED)))
        validatorItemOwner(booking.getItem().getOwner(), userId);

        if (!booking.getStatus().equals(WAITING) &&
                !booking.getStatus().equals(CANCELED)) {
            throw new IncorrectParameterException("BookingStatus");
        }
        if (approved) {
            booking.setStatus(APPROVED);
            log.info("Пользователь с id = {} подтвердил бронирование с id = {}", userId, bookingId);
        } else {
            booking.setStatus(REJECTED);
            log.info("Пользователь с id = {} отклонил бронирование с id = {}", userId, bookingId);
        }
        if (booking.getStatus().equals(CANCELED)) {
            throw new ValidationException("Бронирование было отменено!");
        } //else {
          //  throw new ValidationException("Подтвердить бронирование может только владелец вещи!");
        //}
    //}

            return mapper.toBookingDto(repository.save(booking));
}

    @Override
    public BookingDto getBookingById(Long bookingId, Long userId) {
        validatorUserId(userId);
        Booking booking = repository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с ID=" + bookingId + " не найдено!"));
        //validatorItemOwner(booking.getItem().getOwner(), userId);
        if (booking.getBooker().getId().equals(userId) || validatorItemOwner(booking.getItem().getOwner(), userId)) {
            return mapper.toBookingDto(booking);
        } else {
            throw new NotFoundException("Посмотреть данные бронирования может владелец вещи или" +
                    " пользователь бронирующий ее!");
        }
    }

    @Override
    public List<BookingDto> getBookings(String state, Long userId) {
        validatorUserId(userId);
        List<Booking> bookings;
        Sort sortByStartDesc = Sort.by(Sort.Direction.DESC, "start");
        switch (state) {
            case "ALL":
                bookings = repository.findByBookerId(userId, sortByStartDesc);
                break;
            case "CURRENT":
                bookings = repository.findByBookerIdAndStartIsBeforeAndEndIsAfter(userId, LocalDateTime.now(),
                        LocalDateTime.now(), sortByStartDesc);
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
        validatorUserId(userId);
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
                mapper.toBookingShortDto(repository.findFirstByItem_IdAndEndBeforeOrderByEndDesc(itemId,
                        LocalDateTime.now()));
        return bookingShortDto;
    }

    @Override
    public BookingShortDto getNextBooking(Long itemId) {
        BookingShortDto bookingShortDto =
                mapper.toBookingShortDto(repository.findFirstByItem_IdAndStartAfterOrderByStartAsc(itemId,
                        LocalDateTime.now()));
        return bookingShortDto;
    }

    @Override
    public Booking getBookingWithUserBookedItem(Long itemId, Long userId) {
        return repository.findFirstByItem_IdAndBooker_IdAndEndIsBeforeAndStatus(itemId,
                userId, LocalDateTime.now(), APPROVED);
    }

    public void validatorUserId(Long userId) {
        if (!userRepository.findById(userId).isPresent()) {
            log.info("Пользователя с id = {} нет", userId);
            throw new NotFoundException("Пользователя с id = " + userId + " нет.");
        }
    }

    public void validatorItemId(Long itemId) {
        if (!itemRepository.findById(itemId).isPresent()) {
            log.info("Вещь с id = {} нет", itemId);
            throw new NotFoundException("Вещь с id = " + itemId + " не существует.");
        }
    }


    public void validatorItemAvailable(Long itemId) {
        if (!itemRepository.findById(itemId).get().getAvailable()) {
            log.info("Запрошенная вещь с id = {} не доступна для бронирования", itemId);
            throw new IncorrectParameterException("available (вещь забронирована)");
        }
    }

    public void validatorStartEndTime(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null || start.isAfter(end) || start.equals(end) || start.isBefore(LocalDateTime.now())) {
            log.info("Ошибка во времени бронирования start = {} или end = {} ", start, end);
            throw new IncorrectParameterException("start или end");
        }
    }

    public boolean validatorItemOwner(Long ownerId, Long userId) {
        if (ownerId == null) {
            log.info("У пользователя с id = {} нет вещей.", userId);
            throw new NotFoundException("У пользователя с id = " + userId + " нет вещей.");
        }
        if (!Objects.equals(ownerId, userId)) {
            log.info("Пользователь с id = {} не владеет запрошенной вещью.", userId);
            throw new NotFoundException("Только владелец может подтвердить бронирование");
        }
        return true;
    }

}
