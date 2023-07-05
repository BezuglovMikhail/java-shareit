package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.request.RequestMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class RequestMapperTest {
    private RequestMapper mapper;
    private RequestDto requestDtoSave;
    private Request requestSave;
    private Request requestSave2;
    private User user;
    private UserDto userDto;
    ItemDto itemDto;
    private LocalDateTime createdRequest;
    private List<ItemDto> itemDtoList;

    List<CommentDto> commentDtoList = new ArrayList<>();

    @BeforeEach
    void setUp() {

        user = new User(
                1L,
                "nameTest",
                "test@mail.com"
        );
        userDto = new UserDto(
                1L,
                "nameTest",
                "test@mail.com"
        );

        itemDto = new ItemDto(
                1L,
                "Колотушка",
                "Создаёт шум",
                true,
                1L,
                null,
                null,
                null,
                commentDtoList);

        itemDtoList = new ArrayList<>();

        createdRequest = LocalDateTime.now();

        requestSave = new Request(
                1L,
                "Нужна мощная штука, чтобы аккуратно собирать мебель, желательно из дерева.",
                user,
                createdRequest);

        requestSave2 = new Request(
                null,
                "Нужна мощная штука, чтобы аккуратно собирать мебель, желательно из дерева.",
                user,
                createdRequest);

        requestDtoSave = new RequestDto(
                1L,
                "Нужна мощная штука, чтобы аккуратно собирать мебель, желательно из дерева.",
                userDto,
                createdRequest,
                itemDtoList);
    }

    @Test
    void toRequestDto() {
        RequestDto requestDtoTest = mapper.toRequestDto(requestSave, userDto, itemDtoList);

        assertEquals(requestDtoSave, requestDtoTest);
    }

    @Test
    void toRequest() {
        Request requestTest = mapper.toRequest(requestDtoSave, user, createdRequest);

        assertEquals(requestSave2, requestTest);
    }
}
