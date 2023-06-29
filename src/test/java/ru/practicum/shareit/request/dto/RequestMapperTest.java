package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class RequestMapperTest {

    @Mock
    private UserMapper userMapperMock;
    @Mock
    private UserService userServiceMock;
    @Mock
    private ItemService itemServiceMock;

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
        mapper = new RequestMapper(userMapperMock, userServiceMock, itemServiceMock);

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

        when(userMapperMock.toUserDto(any())).thenReturn(userDto);
        when(itemServiceMock.getItemsByRequestId(any())).thenReturn(itemDtoList);

        RequestDto requestDtoTest = mapper.toRequestDto(requestSave);

        assertEquals(requestDtoSave, requestDtoTest);
    }

    @Test
    void toRequest() {
        Long creatorRequestId = 1L;

        when(userMapperMock.toUser(any())).thenReturn(user);

        Request requestTest = mapper.toRequest(requestDtoSave, creatorRequestId, createdRequest);

        assertEquals(requestSave2, requestTest);
    }
}
