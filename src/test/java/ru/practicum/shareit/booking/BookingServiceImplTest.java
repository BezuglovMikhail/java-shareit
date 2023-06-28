package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepositoryMock;
    @Mock
    private UserService userServiceMock;
    @Mock
    private ItemService itemServiceMock;
    @Mock
    private BookingMapper mapperMock;
    @Mock
    private CommentRepository commentRepositoryMock;

    private BookingService bookingService;

    User user;
    UserDto userDto;
    Item itemSave;
    Item itemSave2;
    Item itemUpdate;
    List<Item> itemList;
    ItemDto itemDtoSave;
    ItemDto itemDtoSave2;
    ItemDto itemDtoFalse;
    ItemDto itemDtoUpdate;
    List<ItemDto> itemDtoList;
    BookingShortDto lastBooking;
    BookingShortDto nextBooking;
    Comment comment;
    CommentDto commentDto;
    Comment comment2;
    CommentDto commentDto2;
    List<CommentDto> commentDtoList = new ArrayList<>();
    LocalDateTime start = LocalDateTime.of(2023, 6, 27, 12, 00, 0);
    LocalDateTime end = LocalDateTime.of(2023, 6, 29, 12, 00, 0);
    Booking booking;

    @BeforeEach
    void setUp() {
        bookingService = new BookingServiceImpl(bookingRepositoryMock,
                userServiceMock,
                itemServiceMock,
                mapperMock);

        itemSave = new Item(
                1L,
                "?????????",
                "??????? ???",
                true,
                1L,
                null);

        itemSave2 = new Item(
                2L,
                "??????",
                "?????????? ???????",
                true,
                1L,
                1L);

        itemUpdate = new Item(
                1L,
                "?????????",
                "????? ???????",
                false,
                1L,
                null);

        itemDtoSave = new ItemDto(
                1L,
                "?????????",
                "??????? ???",
                true,
                1L,
                null,
                null,
                null,
                commentDtoList);

        itemDtoSave2 = new ItemDto(
                2L,
                "??????",
                "?????????? ???????",
                true,
                1L,
                1L,
                null,
                null,
                commentDtoList);

        itemDtoUpdate = new ItemDto(
                1L,
                "?????????",
                "????? ???????",
                false,
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

        itemList = List.of(itemSave, itemSave2);
        itemDtoList = List.of(itemDtoSave, itemDtoSave2);

        booking = new Booking(
                1L,
                start,
                end,
                itemSave,
                user,
                BookingStatus.WAITING
        );

        comment = new Comment(
                1L,
                "????? ?????????, ???? ??????? ???????????!",
                itemSave,
                user,
                LocalDateTime.of(2023, 6, 27, 15, 00, 0)
        );

        commentDto = new CommentDto(1L,
                "????? ?????????, ???? ??????? ???????????!",
                itemSave,
                "nameTest",
                LocalDateTime.of(2023, 6, 27, 15, 00, 0));

        comment2 = new Comment(2L,
                "????? ???????!!! ?? ??????????!",
                itemSave,
                user,
                LocalDateTime.of(2023, 6, 27, 20, 00, 0)
        );

        commentDto2 = new CommentDto(2L,
                "????? ???????!!! ?? ??????????!",
                itemSave,
                "nameTest",
                LocalDateTime.of(2023, 6, 27, 20, 00, 0));
    }


    @Test
    void save() {
    }

    @Test
    void update() {
    }

    @Test
    void getBookingById() {
    }

    @Test
    void getBookings() {
    }

    @Test
    void getBookingsOwner() {
    }

    @Test
    void getLastBooking() {
    }

    @Test
    void getNextBooking() {
    }

    @Test
    void getBookingWithUserBookedItem() {
    }
}
