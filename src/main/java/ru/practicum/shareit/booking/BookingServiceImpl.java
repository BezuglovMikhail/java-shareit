package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.IncorrectParameterException;
import ru.practicum.shareit.NotFoundException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

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

}
