package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JsonTest
@ExtendWith(MockitoExtension.class)
class RequestDtoSerializeTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerializeTest() throws Exception {
        UserDto userDto = new UserDto(
                5L,
                "nameSerialize",
                "emailSerialize"
        );

        RequestDto requestDtoSerialize = new RequestDto(
                1L,
                "descriptionSerialize",
                userDto,
                LocalDateTime.of(2023, 7, 1, 17, 28),
                List.of()
        );

        String expectedResult = "{\"id\":1," +
                "\"description\":\"descriptionSerialize\"," +
                "\"creator\":{\"id\":5," +
                "\"name\":\"nameSerialize\"," +
                "\"email\":\"emailSerialize\"}," +
                "\"created\":\"2023-07-01T17:28:00\"," +
                "\"items\":[]}";

        assertEquals(expectedResult, objectMapper.writeValueAsString(requestDtoSerialize));
    }

    @Test
    void testDeserializeTest() throws Exception {
        String requestDtoString = "{\"id\":1," +
                "\"description\":\"descriptionSerialize\"," +
                "\"creator\":{\"id\":5," +
                "\"name\":\"nameSerialize\"," +
                "\"email\":\"emailSerialize\"}," +
                "\"created\":\"2023-07-01T17:28:00\"," +
                "\"items\":[]}";

        UserDto userDto = new UserDto(
                5L,
                "nameSerialize",
                "emailSerialize"
        );

        RequestDto requestDto = new RequestDto(
                1L,
                "descriptionSerialize",
                userDto,
                LocalDateTime.of(2023, 7, 1, 17, 28),
                List.of()
        );

        RequestDto expectedResult = objectMapper.readValue(requestDtoString, RequestDto.class);

        assertEquals(requestDto, expectedResult);
    }
}
