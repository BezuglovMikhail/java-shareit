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
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    private UserDto userDto;

    private UserDto userDtoUpdate;

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
                1L,
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
    }

    @Test
    void getAllItemByIdUser() {
    }

    @Test
    void getItemById() {
    }

    @Test
    void search() {
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
                        .header(USER_ID, 1))

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
    void deleteUser() {
    }

    @Test
    void createComment() {
    }
}