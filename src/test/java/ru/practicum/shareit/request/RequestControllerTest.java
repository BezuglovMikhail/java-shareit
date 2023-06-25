package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RequestController.class)
class RequestControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    RequestService requestService;

    @Autowired
    private MockMvc mvc;

    private static final String USER_ID = "X-Sharer-User-Id";

    UserDto userDto;

    LocalDateTime created;

    Clock clock;
    RequestDto requestDto;

    RequestDto requestDtoUpdate;

    List<RequestDto> requestDtoList;

    List<ItemDto> itemDtoList = new ArrayList<>();

    List<ItemDto> itemDtoListFull;


    @BeforeEach
    void setUp() {

        userDto = new UserDto(1L, "Потап", "test@mail.com");

        created = LocalDateTime.of(2023, 6, 25, 14, 19, 30);

        itemDtoListFull = List.of(
                new ItemDto(1L,
                        "Киянка",
                        "Деревянный молоток",
                        false,
                        1L,
                        null,
                        null,
                        null,
                        null),

                new ItemDto(2L,
                        "Колотушка",
                        "Большая деревянная палка",
                        false,
                        2L,
                        null,
                        null,
                        null,
                        null)
        );

        requestDto = new RequestDto(1L, "Нужен деревянный молоток", userDto, LocalDateTime.now(), itemDtoList);

        requestDtoUpdate = new RequestDto(2L, "Нужен деревянный молоток", userDto, created, itemDtoListFull);

        requestDtoList = List.of(requestDto, requestDtoUpdate);
    }

    @Test
    void createRequestTest() throws Exception {
        Long userIdTest = 1L;

        when(requestService.create(any(), any(Long.class), any(LocalDateTime.class)))
                .thenReturn(requestDto);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, userIdTest))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$.creator", is(requestDto.getCreator()), UserDto.class))
                .andExpect(jsonPath("$.created",
                        is(requestDto.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.items", is(requestDto.getItems()), List.class));
    }

    @Test
    void getRequestByIdTest() throws Exception {
        Long userIdTest = 1L;
        Long requestIdTest = 1L;

        when(requestService.getRequestById(any(Long.class), any(Long.class)))
                .thenReturn(requestDto);

        mvc.perform(get("/requests/{requestId}", requestIdTest)
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, userIdTest))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$.creator", is(requestDto.getCreator()), UserDto.class))
                .andExpect(jsonPath("$.created",
                        is(requestDto.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.items", is(requestDto.getItems()), List.class));

        Mockito.verify(requestService, Mockito.times(1))
               .getRequestById(userIdTest, requestIdTest);

        Mockito.verifyNoMoreInteractions(requestService);
    }

    @Test
    void getOwnItemRequestsTest() throws Exception {
        Long ownerIdTest = 1L;

        when(requestService.getOwnRequests(any(Long.class)))
                .thenReturn(requestDtoList);

        mvc.perform(get("/requests")
                        .content(mapper.writeValueAsString(requestDtoList))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, ownerIdTest))

                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(requestDtoList)));

        List<RequestDto> requstDtosTestList = requestService.getOwnRequests(ownerIdTest);

        assertEquals(requestDtoList, requstDtosTestList);

        Mockito.verify(requestService, Mockito.times(2))
                .getOwnRequests(ownerIdTest);

        Mockito.verifyNoMoreInteractions(requestService);

    }

    @Test
    void getAllItemRequestsTest() throws Exception {
        Long userIdTest = 1L;
        Integer from = 0;
        Integer size = 10;

        when(requestService.getAllRequests(any(Long.class), any(Integer.class), any(Integer.class)))
                .thenReturn(requestDtoList);

        mvc.perform(get("/requests/all?from={from}&size={size}", from, size)
                        .content(mapper.writeValueAsString(requestDtoList))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, userIdTest))

                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(requestDtoList)));

        List<RequestDto> requstDtosTestList = requestService.getAllRequests(userIdTest, from, size);

        assertEquals(requestDtoList, requstDtosTestList);

        Mockito.verify(requestService, Mockito.times(2))
                .getAllRequests(userIdTest, from, size);

        Mockito.verifyNoMoreInteractions(requestService);
    }
}
