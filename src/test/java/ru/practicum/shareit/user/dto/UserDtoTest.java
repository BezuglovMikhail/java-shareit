package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;


import static org.junit.jupiter.api.Assertions.*;

@JsonTest
@ExtendWith(MockitoExtension.class)
class UserDtoTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerializeTest() throws Exception {
        UserDto userDtoSerialize = new UserDto(
                5L,
                "nameSerialize",
                "emailSerialize@mail.com"
        );

        String expectedResult = "{\"id\":5,\"name\":\"nameSerialize\",\"email\":\"emailSerialize@mail.com\"}";

        assertEquals(expectedResult, objectMapper.writeValueAsString(userDtoSerialize));
    }

    @Test
    void testDeserializeTest() throws Exception {
        String usrdDtoString = "{\"id\":5,\"name\":\"nameSerialize\",\"email\":\"emailSerialize@mail.com\"}";

        UserDto userDtoTest = new UserDto(
                5L,
                "nameSerialize",
                "emailSerialize@mail.com"
        );

        UserDto expectedResult = objectMapper.readValue(usrdDtoString, UserDto.class);

        assertEquals(userDtoTest, expectedResult);
    }
}
