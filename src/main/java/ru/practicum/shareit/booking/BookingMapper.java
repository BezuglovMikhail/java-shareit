package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.service.UserServiceImpl;

import static ru.practicum.shareit.item.dto.ItemMapper.toItemDto;
import static ru.practicum.shareit.user.dto.UserMapper.toUserDto;

@Component
public class BookingMapper {
    private UserServiceImpl userService;
    private ItemServiceImpl itemService;
    //private UserMapper userMapper;
    //private ItemMapper itemMapper;

    @Autowired
    public BookingMapper(UserServiceImpl userService, ItemServiceImpl itemService) {
        this.userService = userService;
        this.itemService = itemService;
    }

    public BookingDto toBookingDto(Booking booking) {
        if (booking != null) {
            return new BookingDto(
                    booking.getId(),
                    booking.getStart(),
                    booking.getEnd(),
                    toItemDto(booking.getItem()),
                    toUserDto(booking.getBooker()),
                    booking.getStatus()
            );
        } else {
            return null;
        }
    }

    public BookingShortDto toBookingShortDto(Booking booking) {
        if (booking != null) {
            return new BookingShortDto(
                    booking.getId(),
                    booking.getBooker().getId(),
                    booking.getStart(),
                    booking.getEnd()
            );
        } else {
            return null;
        }
    }

    public Booking toBooking(BookingInputDto bookingInputDto, Long bookerId) {
        return new Booking(
                null,
                bookingInputDto.getStart(),
                bookingInputDto.getEnd(),
                itemService.findItemById(bookingInputDto.getItemId()),
                userService.findUserById(bookerId),
                BookingStatus.WAITING
        );
    }
}
