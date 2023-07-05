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
class BookingShortDtoTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerializeTest() throws Exception {
        BookingShortDto bookingShortDtoSerialize = new BookingShortDto(
                1L,
                1L,
                LocalDateTime.of(2023, 6, 15, 19, 58),
                LocalDateTime.of(2023, 6, 30, 12, 0)
        );

        String expectedResult = "{" +
                "\"id\":1," +
                "\"bookerId\":1," +
                "\"startTime\":\"2023-06-15T19:58:00\"," +
                "\"endTime\":\"2023-06-30T12:00:00\"" +
                "}";

        assertEquals(expectedResult, objectMapper.writeValueAsString(bookingShortDtoSerialize));
    }

    @Test
    void testDeserializeTest() throws Exception {
        BookingShortDto bookingShortDto = new BookingShortDto(
                1L,
                1L,
                LocalDateTime.of(2023, 6, 15, 19, 58),
                LocalDateTime.of(2023, 6, 30, 12, 0)
        );

        String bookingShotDtoString = "{" +
                "\"id\":1," +
                "\"bookerId\":1," +
                "\"startTime\":\"2023-06-15T19:58:00\"," +
                "\"endTime\":\"2023-06-30T12:00:00\"" +
                "}";

        BookingShortDto expectedResult = objectMapper.readValue(bookingShotDtoString, BookingShortDto.class);

        assertEquals(bookingShortDto, expectedResult);
    }
}
