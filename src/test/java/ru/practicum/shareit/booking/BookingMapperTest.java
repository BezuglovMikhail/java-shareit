package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class BookingMapperTest {

    @Mock
    private ItemMapper itemMapperMock;

    @Mock
    private UserMapper userMapperMock;

    BookingMapper mapper;

    User user;

    Item item;
    UserDto userDto;
    Item itemSave;
    ItemDto itemDto;
    BookingShortDto bookingShortDto;
    BookingInputDto bookingInputDto;

    BookingDto bookingDto;
    List<CommentDto> commentDtoList = new ArrayList<>();
    LocalDateTime start = LocalDateTime.now().plus(Period.ofDays(1));
    LocalDateTime end = LocalDateTime.now().plus(Period.ofDays(5));
    Booking booking;
    Booking bookingSave;

    @BeforeEach
    void setUp() {
        mapper = new BookingMapper(itemMapperMock, userMapperMock);

        itemSave = new Item(
                1L,
                "Колотушка",
                "Создаёт шум",
                true,
                1L,
                null);

        itemDto = new ItemDto(
                1L,
                "Колотушка",
                "Создаёт шум",
                true,
                1L,
                null,
                null,
                null,
                commentDtoList);

        userDto = new UserDto(
                1L,
                "nameTest",
                "test@mail.com"
        );

        user = new User(
                1L,
                "nameTest",
                "test@mail.com"
        );

        item = new Item(1L,
                "Колотушка",
                "Создаёт шум",
                true,
                1L,
                null);

        itemDto = new ItemDto(
                1L,
                "Колотушка",
                "Создаёт шум",
                true,
                2L,
                null,
                null,
                null,
                commentDtoList);

        booking = new Booking(
                1L,
                start,
                end,
                itemSave,
                user,
                BookingStatus.WAITING
        );

        bookingSave = new Booking(
                null,
                start,
                end,
                item,
                user,
                BookingStatus.WAITING);

        bookingDto = new BookingDto(
                1L,
                start,
                end,
                itemDto,
                userDto,
                BookingStatus.WAITING
        );

        bookingShortDto = new BookingShortDto(
                1L,
                1L,
                start,
                end);

        bookingInputDto = new BookingInputDto(
                1L,
                start,
                end);
    }

    @Test
    void toBookingDto() {

        when(itemMapperMock.toItemDto(any())).thenReturn(itemDto);
        when(userMapperMock.toUserDto(any())).thenReturn(userDto);

        BookingDto bookingDtoTest = mapper.toBookingDto(booking);

        assertEquals(bookingDto, bookingDtoTest);
    }

    @Test
    void toBookingShortDto() {

        BookingShortDto bookingShortDtoTest = mapper.toBookingShortDto(booking);

        assertEquals(bookingShortDto, bookingShortDtoTest);
    }

    @Test
    void toBooking() {
        when(itemMapperMock.toItem(any())).thenReturn(item);
        when(userMapperMock.toUser(any())).thenReturn(user);

        Booking bookingTest = mapper.toBooking(bookingInputDto, itemDto, userDto);

        assertEquals(bookingSave, bookingTest);
    }
}
