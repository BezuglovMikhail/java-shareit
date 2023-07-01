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
class userDtoTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerializeTest() throws Exception {
        UserDto userDtoSerialize = new UserDto(
                5L,
                "nameSerialize",
                "emailSerialize"
        );

        String expectedResult = "{\"id\":5,\"name\":\"nameSerialize\",\"email\":\"emailSerialize\"}";

        assertEquals(expectedResult, objectMapper.writeValueAsString(userDtoSerialize));
    }

    @Test
    void testDeserializeTest() throws Exception {
        String usrdDtoString = "{\"id\":5,\"name\":\"nameSerialize\",\"email\":\"emailSerialize\"}";

        UserDto userDtoTest = new UserDto(
                5L,
                "nameSerialize",
                "emailSerialize"
        );

        UserDto expectedResult = objectMapper.readValue(usrdDtoString, UserDto.class);

        assertEquals(userDtoTest, expectedResult);
    }
}
