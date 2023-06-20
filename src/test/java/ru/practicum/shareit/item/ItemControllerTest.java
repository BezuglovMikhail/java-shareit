package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    private ItemMapper itemMapper;

    private MockMvc mvc;

    private ItemDto itemDto;

    private ItemDto itemDtoUpdate;

    private List<ItemDto> itemsDto;

    private static final String USER_ID = "X-Sharer-User-Id";


    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        itemDto = new ItemDto(1L,
                "Дрель-тест",
                "Дрель на ручной тяге, раритет!",
                false,
                2L,
                null,
                null,
                null,
                null
        );

        itemDtoUpdate = new ItemDto(1L,
                "Дрель-тест-update",
                "Аккумуляторная дрель + запасной аккумулятор.",
                true,
                1L,
                null,
                null,
                null,
                null
        );

        itemsDto = List.of(
                new ItemDto(
                        1L,
                        "Дрель-тест",
                        "Дрель на ручной тяге, раритет!",
                        false,
                        2L,
                        null,
                        null,
                        null,
                        null),
                new ItemDto(
                        2L,
                        "Отвертка-тест",
                        "Простая отвертка, от деда!",
                        true,
                        2L,
                        null,
                        null,
                        null,
                        null),
                new ItemDto(
                        3L,
                        "Киянка",
                        "Мощная, 5 кг.!",
                        true,
                        2L,
                        null,
                        null,
                        null,
                        null)
        );
    }

    @Test
    void getAllItemByIdUser() throws Exception {
        Long ownerIdTest = 2L;

        when(itemService.findAllItemByIdUser(any(Long.class)))
                .thenReturn(itemsDto);

        mvc.perform(get("/items")
                        .content(mapper.writeValueAsString(itemsDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID, ownerIdTest))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemsDto)))
        ;

        List<ItemDto> itemsTestList = itemService.findAllItemByIdUser(ownerIdTest);

        assertEquals(itemsDto, itemsTestList);

        Mockito.verify(itemService, Mockito.times(2))
                .findAllItemByIdUser(ownerIdTest);

        Mockito.verifyNoMoreInteractions(itemService);
    }

    @Test
    void getItemByIdTest() throws Exception {
        Long itemId = 1L;
        Long ownerIdTest = 2L;

        when(itemService.getItemById(any(Long.class), any(Long.class)))
                .thenReturn(itemDto);

        mvc.perform(get("/items/{itemId}", itemId)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, ownerIdTest))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.owner", is(itemDto.getOwner()), Long.class))
                .andExpect(jsonPath("$.requestId", is(itemDto.getRequestId())))
                .andExpect(jsonPath("$.lastBooking", is(itemDto.getLastBooking())))
                .andExpect(jsonPath("$.nextBooking", is(itemDto.getNextBooking())))
                .andExpect(jsonPath("$.comments", is(itemDto.getComments())))
        ;

        Mockito.verify(itemService, Mockito.times(1))
                .getItemById(itemId, ownerIdTest);

        Mockito.verifyNoMoreInteractions(itemService);
    }

    @Test
    void searchTest() throws Exception {

        String textSearch = "text";
        Long ownerIdTest = 2L;

        when(itemService.searchItems(textSearch))
                .thenReturn(itemsDto);

        mvc.perform(get("/items/search?text={text}", textSearch)
                        .content(mapper.writeValueAsString(itemsDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID, ownerIdTest))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemsDto)))
        ;

        List<ItemDto> itemsTestList = itemService.searchItems(textSearch);

        assertEquals(itemsDto, itemsTestList);

        Mockito.verify(itemService, Mockito.times(2))
                .searchItems(textSearch);

        Mockito.verifyNoMoreInteractions(itemService);
    }

    @Test
    void saveItemTest() throws Exception {
        Long ownerIdTest = 1L;

        when(itemService.save(any(), any(Long.class)))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.owner", is(itemDto.getOwner()), Long.class))
                .andExpect(jsonPath("$.requestId", is(itemDto.getRequestId())))
                .andExpect(jsonPath("$.lastBooking", is(itemDto.getLastBooking())))
                .andExpect(jsonPath("$.nextBooking", is(itemDto.getNextBooking())))
                .andExpect(jsonPath("$.comments", is(itemDto.getComments())))
        ;

        Mockito.verify(itemService, Mockito.times(1))
                .save(itemDto, ownerIdTest);

        Mockito.verifyNoMoreInteractions(itemService);
    }

    @Test
    void updateItemTest() throws Exception {
        Long ownerIdTest = 1L;
        Long itemId = 1L;

        when(itemService.updateItem(any(), any(Long.class), any(Long.class)))
                .thenReturn(itemDtoUpdate);

        mvc.perform(patch("/items/{itemId}", itemId)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, ownerIdTest))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoUpdate.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDtoUpdate.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoUpdate.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDtoUpdate.getAvailable())))
                .andExpect(jsonPath("$.owner", is(itemDtoUpdate.getOwner()), Long.class))
                .andExpect(jsonPath("$.requestId", is(itemDtoUpdate.getRequestId())))
                .andExpect(jsonPath("$.lastBooking", is(itemDtoUpdate.getLastBooking())))
                .andExpect(jsonPath("$.nextBooking", is(itemDtoUpdate.getNextBooking())))
                .andExpect(jsonPath("$.comments", is(itemDtoUpdate.getComments())))
        ;

        Mockito.verify(itemService, Mockito.times(1))
                .updateItem(itemDto, ownerIdTest, itemId);

        Mockito.verifyNoMoreInteractions(itemService);
    }

    @Test
    void deleteUserTest() throws Exception {
        Long ownerIdTest = 2L;
        Long itemId = 1L;

        mvc.perform(delete("/items/{itemsId}", itemId)
                        .header(USER_ID, ownerIdTest))
                .andExpect(status().isOk())
        ;

        Mockito.verify(itemService, Mockito.times(1))
                .deleteItem(itemId, ownerIdTest);

        Mockito.verifyNoMoreInteractions(itemService);
    }

    @Test
    void createComment() throws Exception{
        Long ownerIdTest = 1L;
        Long itemId = 1L;

        Item itemTest = new Item(
                1L,
                "Отвертка",
                "Крестовая",
                true,
                ownerIdTest,
                1L
        );

        CommentDto commentDto = new CommentDto(
                1L,
                "Качественная вещь",
                itemTest,
                "Гриша",
                LocalDateTime.of(2023, 6, 19, 22, 23, 3)
        );

        when(itemService.saveComment(any(), any(Long.class), any(Long.class)))
                .thenReturn(commentDto);

        mvc.perform(post("/items/{itemId}/comment", itemId)
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, ownerIdTest))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())))
                .andExpect(jsonPath("$.createdTime", is(commentDto.getCreatedTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
        ;

        Mockito.verify(itemService, Mockito.times(1))
                .saveComment(commentDto, itemId, ownerIdTest);

        Mockito.verifyNoMoreInteractions(itemService);
    }
}