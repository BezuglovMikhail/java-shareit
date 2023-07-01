package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JsonTest
@ExtendWith(MockitoExtension.class)
class BookingDtoTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerializeTest() throws Exception {

        BookingShortDto lastBooking = new BookingShortDto();

        BookingShortDto nextBooking = new BookingShortDto();

        Item item = new Item(
                7L,
                "NameItem",
                "DescriptionItem",
                true,
                1L,
                3L
        );
        UserDto booker = new UserDto(
                5L,
                "nameSerialize",
                "emailSerialize@mail.com"
        );

        List<CommentDto> commentDtoList = List.of(new CommentDto(
                5L,
                "commentTextTest",
                item,
                booker.getName(),
                LocalDateTime.of(2023, 7, 1, 19, 58)
        ));

        ItemDto itemDto = new ItemDto(
                1L,
                "nameItem",
                "descriptionSerialize",
                true,
                1L,
                1L,
                lastBooking,
                nextBooking,
                commentDtoList
        );

        BookingDto bookingDtoSerialize = new BookingDto(
                1L,
                LocalDateTime.of(2023, 6, 15, 19, 58),
                LocalDateTime.of(2023, 6, 30, 12, 0),
                itemDto,
                booker,
                BookingStatus.WAITING
        );

        String expectedResult = "{" +
                "\"id\":1," +
                "\"start\":\"2023-06-15T19:58:00\"," +
                "\"end\":\"2023-06-30T12:00:00\"," +
                "\"item\":{" +
                    "\"id\":1," +
                    "\"name\":\"nameItem\"," +
                    "\"description\":\"descriptionSerialize\"," +
                    "\"available\":true," +
                    "\"owner\":1," +
                    "\"requestId\":1," +
                    "\"lastBooking\":{\"id\":null," +
                    "\"bookerId\":null," +
                    "\"startTime\":null," +
                    "\"endTime\":null}," +
                    "\"nextBooking\":{" +
                    "\"id\":null," +
                    "\"bookerId\":null," +
                    "\"startTime\":null," +
                    "\"endTime\":null}," +
                    "\"comments\":[" +
                            "{" +
                        "\"id\":5," +
                        "\"text\":\"commentTextTest\"," +
                        "\"authorName\":\"nameSerialize\"," +
                        "\"created\":\"2023-07-01T19:58:00\"" +
                            "}" +
                                "]" +
                    "}," +
                "\"booker\":{" +
                    "\"id\":5," +
                    "\"name\":\"nameSerialize\"," +
                    "\"email\":\"emailSerialize@mail.com\"" +
                    "}," +
                "\"status\":\"WAITING\"" +
                "}";

        assertEquals(expectedResult, objectMapper.writeValueAsString(bookingDtoSerialize));
    }

    @Test
    void testDeserializeTest() throws Exception {

        BookingShortDto lastBooking = new BookingShortDto();
        BookingShortDto nextBooking = new BookingShortDto();

        UserDto booker = new UserDto(
                5L,
                "nameSerialize",
                "emailSerialize@mail.com"
        );

        List<CommentDto> commentDtoList = List.of(new CommentDto(
                5L,
                "commentTextTest",
                null,
                booker.getName(),
                LocalDateTime.of(2023, 7, 1, 19, 58)
        ));

        ItemDto itemDto = new ItemDto(
                1L,
                "nameItem",
                "descriptionSerialize",
                true,
                1L,
                1L,
                lastBooking,
                nextBooking,
                commentDtoList
        );

        BookingDto bookingDto = new BookingDto(
                1L,
                LocalDateTime.of(2023, 6, 15, 19, 58),
                LocalDateTime.of(2023, 6, 30, 12, 0),
                itemDto,
                booker,
                BookingStatus.WAITING
        );

        String bookingDtoString = "{" +
                "\"id\":1," +
                "\"start\":\"2023-06-15T19:58:00\"," +
                "\"end\":\"2023-06-30T12:00:00\"," +
                "\"item\":{" +
                "\"id\":1," +
                "\"name\":\"nameItem\"," +
                "\"description\":\"descriptionSerialize\"," +
                "\"available\":true," +
                "\"owner\":1," +
                "\"requestId\":1," +
                "\"lastBooking\":{\"id\":null," +
                "\"bookerId\":null," +
                "\"startTime\":null," +
                "\"endTime\":null}," +
                "\"nextBooking\":{" +
                "\"id\":null," +
                "\"bookerId\":null," +
                "\"startTime\":null," +
                "\"endTime\":null}," +
                "\"comments\":[" +
                "{" +
                "\"id\":5," +
                "\"text\":\"commentTextTest\"," +
                "\"authorName\":\"nameSerialize\"," +
                "\"created\":\"2023-07-01T19:58:00\"" +
                "}" +
                "]" +
                "}," +
                "\"booker\":{" +
                "\"id\":5," +
                "\"name\":\"nameSerialize\"," +
                "\"email\":\"emailSerialize@mail.com\"" +
                "}," +
                "\"status\":\"WAITING\"" +
                "}";

        BookingDto expectedResult = objectMapper.readValue(bookingDtoString, BookingDto.class);

        assertEquals(bookingDto, expectedResult);
    }
}
