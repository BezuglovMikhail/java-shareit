package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@JsonTest
@ExtendWith(MockitoExtension.class)
class CommentDtoTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerializeTest() throws Exception {
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
                "emailSerialize"
        );

        CommentDto commentDtoSerialize = new CommentDto(
                5L,
                "commentTextTest",
                item,
                userCreatedComment.getName(),
                LocalDateTime.of(2023, 7, 1, 19, 58)
        );

        String expectedResult = "{" +
                "\"id\":5," +
                "\"text\":\"commentTextTest\"," +
                "\"authorName\":\"nameSerialize\"," +
                "\"created\":\"2023-07-01T19:58:00\"" +
                "}";

        assertEquals(expectedResult, objectMapper.writeValueAsString(commentDtoSerialize));
    }

    @Test
    void testDeserializeTest() throws Exception {
        User userCreatedComment = new User(
                5L,
                "nameSerialize",
                "emailSerialize"
        );

        CommentDto commentDto = new CommentDto(
                5L,
                "commentTextTest",
                null,
                userCreatedComment.getName(),
                LocalDateTime.of(2023, 7, 1, 19, 58)
        );

        String commentDtoString = "{" +
                "\"id\":5," +
                "\"text\":\"commentTextTest\"," +
                "\"authorName\":\"nameSerialize\"," +
                "\"created\":\"2023-07-01T19:58:00\"" +
                "}";

        CommentDto expectedResult = objectMapper.readValue(commentDtoString, CommentDto.class);

        assertEquals(commentDto, expectedResult);
    }
}
