package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exeption.IncorrectParameterException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {

    @Mock
    private RequestRepository repositoryMock;
    @Mock
    private RequestMapper mapperMock;
    @Mock
    private UserService userServiceMock;

    private RequestService requestService;
    private RequestDto requestDtoSave;
    private RequestDto requestDtoSave2;
    private RequestDto requestDtoDescriptionEmpty;
    private Request requestSave;
    private Request requestSave2;
    private List<RequestDto> requestDtoList;
    private List<Request> requestList;
    private User user;
    private UserDto userDto;
    private ItemDto itemDto;
    private ItemDto itemDto2;
    private LocalDateTime createdRequest;
    private List<ItemDto> itemDtoEmptyList;
    private List<ItemDto> itemDtoList;
    private List<CommentDto> commentDtoList;

    @BeforeEach
    void setUp() {
        requestService = new RequestServiceImpl(
                repositoryMock,
                mapperMock,
                userServiceMock);

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

        commentDtoList = new ArrayList<>();

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

        itemDto2 = new ItemDto(
                2L,
                "Киянка",
                "Деревянный молоток",
                true,
                1L,
                1L,
                null,
                null,
                commentDtoList);

        createdRequest = LocalDateTime.of(2023, 6, 27, 22, 00, 0);

        requestSave = new Request(
                1L,
                "Нужна мощная штука, чтобы аккуратно собирать мебель, желательно из дерева.",
                user,
                createdRequest);

        requestSave2 = new Request(
                2L,
                "Нужна громкая штука, чтобы шуметь.",
                user,
                createdRequest);

        requestDtoSave = new RequestDto(
                1L,
                "Нужна мощная штука, чтобы аккуратно собирать мебель, желательно из дерева.",
                userDto,
                createdRequest,
                itemDtoList);

        requestDtoSave2 = new RequestDto(
                2L,
                "Нужна громкая штука, чтобы шуметь.",
                userDto,
                createdRequest,
                itemDtoList);

        requestDtoList = List.of(requestDtoSave, requestDtoSave2);
        requestList = List.of(requestSave, requestSave2);
        /*itemSave = new Item(
                1L,
                "Колотушка",
                "Создаёт шум",
                true,
                1L,
                null);

        itemSave2 = new Item(
                2L,
                "Киянка",
                "Деревянный молоток",
                true,
                1L,
                1L);

        itemUpdate = new Item(
                1L,
                "Колотушка",
                "Почти сломана",
                false,
                1L,
                null);

        itemDtoSave = new ItemDto(
                1L,
                "Колотушка",
                "Создаёт шум",
                true,
                1L,
                null,
                null,
                null,
                commentDtoList);

        itemDtoSave2 = new ItemDto(
                2L,
                "Киянка",
                "Деревянный молоток",
                true,
                1L,
                1L,
                null,
                null,
                commentDtoList);

        itemDtoUpdate = new ItemDto(
                1L,
                "Колотушка",
                "Почти сломана",
                false,
                1L,
                null,
                null,
                null,
                commentDtoList);



        itemList = List.of(itemSave, itemSave2);
        itemDtoList = List.of(itemDtoSave, itemDtoSave2);

        booking = new Booking(
                1L,
                start,
                end,
                itemSave,
                user,
                BookingStatus.WAITING
        );

        comment = new Comment(
                1L,
                "Супер колотушка, всем соседям понравилась!",
                itemSave,
                user,
                LocalDateTime.of(2023, 6, 27, 15, 00, 0)
        );

        commentDto = new CommentDto(1L,
                "Супер колотушка, всем соседям понравилась!",
                itemSave,
                "nameTest",
                LocalDateTime.of(2023, 6, 27, 15, 00, 0));

        comment2 = new Comment(2L,
                "Очень тяжелая!!! Не рекомендую!",
                itemSave,
                user,
                LocalDateTime.of(2023, 6, 27, 20, 00, 0)
        );

        commentDto2 = new CommentDto(2L,
                "Очень тяжелая!!! Не рекомендую!",
                itemSave,
                "nameTest",
                LocalDateTime.of(2023, 6, 27, 20, 00, 0));*/
    }

    @Test
    void create_True_Test() {
        Long creatorRequestId = 1L;

        when(repositoryMock.save(any())).thenReturn(requestSave);
        when(mapperMock.toRequest(requestDtoSave, creatorRequestId, createdRequest)).thenReturn(requestSave);
        when(mapperMock.toRequestDto(requestSave)).thenReturn(requestDtoSave);

        RequestDto requestDtoTest = requestService.create(requestDtoSave, creatorRequestId, createdRequest);

        assertEquals(requestDtoSave, requestDtoTest);
        Mockito.verify(repositoryMock, Mockito.times(1)).save(requestSave);
        Mockito.verify(mapperMock, Mockito.times(1)).toRequestDto(requestSave);
        Mockito.verify(mapperMock, Mockito.times(1)).toRequest(any(),any(),any());
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void create_False_DescriptionEmpty_Test() {
        Long creatorRequestId = 1L;

        requestDtoDescriptionEmpty = new RequestDto(
                1L,
                "",
                userDto,
                createdRequest,
                itemDtoList);

        IncorrectParameterException ex = assertThrows(IncorrectParameterException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                requestService.create(requestDtoDescriptionEmpty, creatorRequestId, createdRequest);
            }
        });

        assertEquals("description", ex.getParameter());
        Mockito.verify(repositoryMock, Mockito.times(0)).save(requestSave);
        Mockito.verify(mapperMock, Mockito.times(0)).toRequestDto(requestSave);
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void getRequestById() {
        Long requestId = 1L;
        Long userId = 1L;

        when(repositoryMock.findById(any())).thenReturn(Optional.of(requestSave));
        when(userServiceMock.findByIdUser(userId)).thenReturn(userDto);
        when(mapperMock.toRequestDto(requestSave)).thenReturn(requestDtoSave);

        RequestDto requestDtoTest = requestService.getRequestById(requestId, userId);

        assertEquals(requestDtoSave, requestDtoTest);
        Mockito.verify(repositoryMock, Mockito.times(1)).findById(requestId);
        Mockito.verify(mapperMock, Mockito.times(1)).toRequestDto(requestSave);
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void getOwnRequests() {
    }

    @Test
    void getAllRequests() {
    }
}