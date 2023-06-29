package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ItemMapperTest {

    @Mock
    private ItemService itemServiceMock;

    private ItemMapper mapper;

    User user;
    UserDto userDto;
    Item itemSave;
    ItemDto itemDtoSave;
    BookingShortDto lastBooking;
    BookingShortDto nextBooking;
    Comment comment;
    CommentDto commentDto;
    List<CommentDto> commentDtoList = new ArrayList<>();
    LocalDateTime start = LocalDateTime.now().plus(Period.ofDays(1));
    LocalDateTime end = LocalDateTime.now().plus(Period.ofDays(5));
    Booking booking;

    @BeforeEach
    void setUp() {
        mapper = new ItemMapper(itemServiceMock);

        itemSave = new Item(
                1L,
                "Колотушка",
                "Создаёт шум",
                true,
                1L,
                null);

        itemDtoSave = new ItemDto(
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
                "Супер колотушка, всем соседям понравилась!",
                itemSave,
                user,
                LocalDateTime.now().minus(Period.ofDays(1))
        );

        commentDto = new CommentDto(1L,
                "Супер колотушка, всем соседям понравилась!",
                itemSave,
                "nameTest",
                LocalDateTime.now().minus(Period.ofDays(1)));
    }

    @Test
    void toItemDto() {

        when(itemServiceMock.getCommentsByItemId(any())).thenReturn(commentDtoList);

        ItemDto itemDtoTest = mapper.toItemDto(itemSave);

        assertEquals(itemDtoSave, itemDtoTest);
    }

    @Test
    void toItem() {

        Item itemTest = mapper.toItem(itemDtoSave);

        assertEquals(itemSave, itemTest);
    }

    @Test
    void toItemExtDto() {

        ItemDto itemDtoTest = mapper.toItemExtDto(itemSave, lastBooking, nextBooking, commentDtoList);

        assertEquals(itemDtoSave, itemDtoTest);
    }

    @Test
    void toCommentDto() {
        CommentDto commentDtoTest = mapper.toCommentDto(comment);

        assertEquals(commentDto, commentDtoTest);
    }
}
