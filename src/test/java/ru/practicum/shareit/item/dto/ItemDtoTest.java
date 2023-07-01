package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JsonTest
@ExtendWith(MockitoExtension.class)
class ItemDtoTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerializeTest() throws Exception {
        BookingShortDto lastBooking = new BookingShortDto(
                1L,
                1L,
                LocalDateTime.of(2023, 6, 15, 19, 58),
                LocalDateTime.of(2023, 6, 30, 12, 0)
        );

        BookingShortDto nextBooking = new BookingShortDto();

        Item item = new Item(
                7L,
                "NameItem",
                "DescriptionItem",
                true,
                1L,
                3L
        );

        User userCreatedComment = new User(
                5L,
                "nameSerialize",
                "emailSerialize@mail.com"
        );

        List<CommentDto> commentDtoList = List.of(new CommentDto(
                5L,
                "commentTextTest",
                item,
                userCreatedComment.getName(),
                LocalDateTime.of(2023, 7, 1, 19, 58)
        ));

        ItemDto itemDtoSerialize = new ItemDto(
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

        String expectedResult = "{\"id\":1," +
                "\"name\":\"nameItem\"," +
                "\"description\":\"descriptionSerialize\"," +
                "\"available\":true," +
                "\"owner\":1," +
                "\"requestId\":1," +
                "\"lastBooking\":" +
                        "{" +
                        "\"id\":1," +
                        "\"bookerId\":1," +
                        "\"startTime\":\"2023-06-15T19:58:00\"," +
                        "\"endTime\":\"2023-06-30T12:00:00\"" +
                        "}," +
                "\"nextBooking\":{" +
                        "\"id\":null," +
                        "\"bookerId\":null," +
                        "\"startTime\":null," +
                        "\"endTime\":null" +
                        "}," +
                "\"comments\":" +
                    "[" +
                        "{" +
                        "\"id\":5," +
                        "\"text\":\"commentTextTest\"," +
                        "\"authorName\":\"nameSerialize\"," +
                        "\"created\":\"2023-07-01T19:58:00\"" +
                        "}" +
                    "]" +
                "}";

        assertEquals(expectedResult, objectMapper.writeValueAsString(itemDtoSerialize));
    }

    @Test
    void testDeserializeTest() throws Exception {
        BookingShortDto lastBooking = new BookingShortDto(
                1L,
                1L,
                LocalDateTime.of(2023, 6, 15, 19, 58),
                LocalDateTime.of(2023, 6, 30, 12, 0)
        );

        BookingShortDto nextBooking = new BookingShortDto();

        Item item = new Item(
                7L,
                "NameItem",
                "DescriptionItem",
                true,
                1L,
                3L
        );

        User userCreatedComment = new User(
                5L,
                "nameSerialize",
                "emailSerialize@mail.com"
        );

        List<CommentDto> commentDtoList = List.of();

        ItemDto itemDtoTest = new ItemDto(
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

        String itemDtoString = "{\"id\":1," +
                "\"name\":\"nameItem\"," +
                "\"description\":\"descriptionSerialize\"," +
                "\"available\":true," +
                "\"owner\":1," +
                "\"requestId\":1," +
                "\"lastBooking\":" +
                "{" +
                "\"id\":1," +
                "\"bookerId\":1," +
                "\"startTime\":\"2023-06-15T19:58:00\"," +
                "\"endTime\":\"2023-06-30T12:00:00\"" +
                "}," +
                "\"nextBooking\":{" +
                "\"id\":null," +
                "\"bookerId\":null," +
                "\"startTime\":null," +
                "\"endTime\":null" +
                "}," +
                "\"comments\":" +
                "[" +
                "]" +
                "}";

        ItemDto expectedResult = objectMapper.readValue(itemDtoString, ItemDto.class);

        assertEquals(itemDtoTest, expectedResult);
    }
}
