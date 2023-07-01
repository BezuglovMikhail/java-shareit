package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@JsonTest
@ExtendWith(MockitoExtension.class)
class BookingInputDtoTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerializeTest() throws Exception {
        BookingInputDto bookingInputDtoSerialize = new BookingInputDto(
                1L,
                LocalDateTime.of(2023, 6, 15, 19, 58),
                LocalDateTime.of(2023, 6, 30, 12, 0)
        );

        String expectedResult = "{" +
                "\"itemId\":1," +
                "\"start\":\"2023-06-15T19:58:00\"," +
                "\"end\":\"2023-06-30T12:00:00\"" +
                "}";

        assertEquals(expectedResult, objectMapper.writeValueAsString(bookingInputDtoSerialize));
    }

    @Test
    void testDeserializeTest() throws Exception {
        BookingInputDto bookingInputDto = new BookingInputDto(
                1L,
                LocalDateTime.of(2023, 6, 15, 19, 58),
                LocalDateTime.of(2023, 6, 30, 12, 0)
        );

        String bookingInputDtoString = "{" +
                "\"itemId\":1," +
                "\"start\":\"2023-06-15T19:58:00\"," +
                "\"end\":\"2023-06-30T12:00:00\"" +
                "}";

        BookingInputDto expectedResult = objectMapper.readValue(bookingInputDtoString, BookingInputDto.class);

        assertEquals(bookingInputDto, expectedResult);
    }
}
