package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.IncorrectParameterException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exeption.ItemNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exeption.UserNotFoundException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceTest {

    private final ItemService itemService;

    @BeforeEach
    void create() {
        itemService.getUserRepository().save(UserDto.builder()
                .email("test1@mail.com")
                .name("name1")
                .build());
        itemService.getUserRepository().save(UserDto.builder()
                .email("test2@mail.com")
                .name("name2")
                .build());
        itemService.getUserRepository().save(UserDto.builder()
                .email("test3@mail.com")
                .name("name3")
                .build());
    }

    @AfterEach
    void clear() {
        itemService.getItemRepository().getItems().clear();
        itemService.getItemRepository().getUsersItemsId().clear();
        itemService.getItemRepository().setId(0);
        itemService.getUserRepository().getUsers().clear();
        itemService.getUserRepository().setIdUser(0);
    }

    @Test
    void saveTrue() {
        ItemDto itemDtoTest = ItemDto.builder()
                .name("Перфоратор")
                .description("Есть ударный режим и набор сверл по бетону")
                .available(false)
                .build();

        ItemDto itemDtoTest2 = ItemDto.builder()
                .name("Молоток")
                .description("Молоток с гвоздодером")
                .available(true)
                .build();

        ItemDto itemDtoTest3 = ItemDto.builder()
                .name("Аккумуляторный шуруповерт")
                .description("шуруповерт + аккумулятор")
                .available(true)
                .build();

        ItemDto itemDto1 = itemService.save(itemDtoTest, 1L);
        ItemDto itemDto2 = itemService.save(itemDtoTest2, 1L);
        ItemDto itemDto3 = itemService.save(itemDtoTest3, 2L);

        assertEquals(1, itemDto1.getId());
        assertEquals(2, itemDto2.getId());
        assertEquals(3, itemDto3.getId());
    }

    @Test
    void saveFalse() {
        ItemDto itemDtoTest = ItemDto.builder()
                .name("")
                .description("Есть ударный режим и набор сверл по бетону")
                .available(false)
                .build();

        ItemDto itemDtoTest2 = ItemDto.builder()
                .name("Молоток")
                .available(true)
                .build();

        ItemDto itemDtoTest3 = ItemDto.builder()
                .name("Аккумуляторный шуруповерт")
                .description("шуруповерт + аккумулятор")
                .build();

        UserNotFoundException ex = assertThrows(UserNotFoundException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                itemService.save(itemDtoTest, 1000L);
            }
        });

        assertEquals("Пользователя с id = 1000 не найден.", ex.getMessage());

        IncorrectParameterException ex1 = assertThrows(IncorrectParameterException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                itemService.save(itemDtoTest, 1L);
            }
        });

        assertEquals("name", ex1.getParameter());

        IncorrectParameterException ex2 = assertThrows(IncorrectParameterException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                itemService.save(itemDtoTest2, 1L);
            }
        });

        assertEquals("description", ex2.getParameter());

        IncorrectParameterException ex3 = assertThrows(IncorrectParameterException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                itemService.save(itemDtoTest3, 3L);
            }
        });

        assertEquals("available", ex3.getParameter());
    }

    @Test
    void findAllItemByIdUser() {
        ItemDto itemDtoTest = ItemDto.builder()
                .name("Перфоратор")
                .description("Есть ударный режим и набор сверл по бетону")
                .available(false)
                .build();

        ItemDto itemDtoTest2 = ItemDto.builder()
                .name("Молоток")
                .description("Молоток с гвоздодером")
                .available(true)
                .build();

        ItemDto itemDtoTest3 = ItemDto.builder()
                .name("Аккумуляторный шуруповерт")
                .description("шуруповерт + аккумулятор")
                .available(true)
                .build();

        ItemDto itemDto1 = itemService.save(itemDtoTest, 1L);
        ItemDto itemDto2 = itemService.save(itemDtoTest2, 1L);
        ItemDto itemDto3 = itemService.save(itemDtoTest3, 2L);

        assertEquals(List.of(itemDto1, itemDto2), itemService.findAllItemByIdUser(1L));
        assertEquals(List.of(itemDto3), itemService.findAllItemByIdUser(2L));
    }

    @Test
    void findAllItemByIdUserFalse() {
        UserNotFoundException ex = assertThrows(UserNotFoundException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                itemService.findAllItemByIdUser(1000L);
            }
        });

        assertEquals("Пользователя с id = 1000 не найден.", ex.getMessage());
    }

    @Test
    void findById() {
        ItemDto itemDtoTest = ItemDto.builder()
                .name("Перфоратор")
                .description("Есть ударный режим и набор сверл по бетону")
                .available(false)
                .build();

        ItemDto itemDtoTest2 = ItemDto.builder()
                .name("Молоток")
                .description("Молоток с гвоздодером")
                .available(true)
                .build();

        ItemDto itemDtoTest3 = ItemDto.builder()
                .name("Аккумуляторный шуруповерт")
                .description("шуруповерт + аккумулятор")
                .available(true)
                .build();

        ItemDto itemDto1 = itemService.save(itemDtoTest, 1L);
        ItemDto itemDto2 = itemService.save(itemDtoTest2, 1L);
        ItemDto itemDto3 = itemService.save(itemDtoTest3, 2L);

        assertEquals(itemDto1, itemService.findById(1L));
        assertEquals(itemDto2, itemService.findById(2L));
        assertEquals(itemDto3, itemService.findById(3L));
    }

    @Test
    void findByIdFalse() {
        ItemDto itemDtoTest = ItemDto.builder()
                .name("Перфоратор")
                .description("Есть ударный режим и набор сверл по бетону")
                .available(false)
                .build();

        ItemNotFoundException ex = assertThrows(ItemNotFoundException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                itemService.findById(1000L);
            }
        });

        assertEquals("Вещь с id = 1000 не найдена.", ex.getMessage());
    }

    @Test
    void deleteItem() {
        UserNotFoundException ex = assertThrows(UserNotFoundException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                itemService.findAllItemByIdUser(1000L);
            }
        });

        assertEquals("Пользователя с id = 1000 не найден.", ex.getMessage());
    }

    @Test
    void updateItem() {
        ItemDto itemDtoTest = ItemDto.builder()
                .name("Перфоратор")
                .description("Есть ударный режим и набор сверл по бетону")
                .available(false)
                .build();

        ItemDto itemDtoTest2 = ItemDto.builder()
                .name("Молоток")
                .description("Молоток с гвоздодером")
                .available(true)
                .build();

        ItemDto itemDtoTest3 = ItemDto.builder()
                .name("Аккумуляторный шуруповерт")
                .description("шуруповерт + аккумулятор")
                .available(true)
                .build();

        itemService.save(itemDtoTest, 1L);
        itemService.save(itemDtoTest2, 1L);
        itemService.save(itemDtoTest3, 2L);

        ItemDto itemDtoTest4 = ItemDto.builder()
                .description("Есть ударный режим без сверл")
                .build();

        UserNotFoundException ex = assertThrows(UserNotFoundException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                itemService.updateItem(itemDtoTest4, 3L, 1L);
            }
        });

        assertEquals("У пользователя с id = " + 3 + " нет вещей.", ex.getMessage());

        ItemNotFoundException ex2 = assertThrows(ItemNotFoundException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                itemService.updateItem(itemDtoTest4, 2L, 1L);
            }
        });

        assertEquals("Вещь с id = 1 у пользователя с id = 2 не найдена.", ex2.getMessage());

    }

    @Test
    void searchItems() {
        ItemDto itemDtoTest = ItemDto.builder()
                .name("Перфоратор")
                .description("Есть ударный режим и набор сверл по бетону")
                .available(false)
                .build();

        ItemDto itemDtoTest2 = ItemDto.builder()
                .name("Молоток")
                .description("Молоток с гвоздодером")
                .available(false)
                .build();

        ItemDto itemDtoTest3 = ItemDto.builder()
                .name("Аккумуляторный шуруповерт")
                .description("шуруповерт + аккумулятор")
                .available(true)
                .build();

        ItemDto itemDtoTest4 = ItemDto.builder()
                .name("Перфоратор")
                .description("Есть ударный режим, без набора сверл")
                .available(true)
                .build();

        ItemDto itemDtoTest5 = ItemDto.builder()
                .name("Молоток")
                .description("Молоток простой")
                .available(true)
                .build();

        itemService.save(itemDtoTest, 1L);
        itemService.save(itemDtoTest2, 1L);
        itemService.save(itemDtoTest3, 2L);
        itemService.save(itemDtoTest4, 3L);
        itemService.save(itemDtoTest5, 3L);

        List<ItemDto> itemsDtoTest = new ArrayList<>(List.of(
                ItemDto.builder()
                        .id(3L)
                        .name("Аккумуляторный шуруповерт")
                        .description("шуруповерт + аккумулятор")
                        .available(true)
                        .build(),
                ItemDto.builder()
                        .id(4L)
                        .name("Перфоратор")
                        .description("Есть ударный режим, без набора сверл")
                        .available(true)
                        .build(),
                ItemDto.builder()
                        .id(5L)
                        .name("Молоток")
                        .description("Молоток простой")
                        .available(true)
                        .build()
        ));

        List<ItemDto> itemsDtoTest2 = new ArrayList<>(List.of(
                ItemDto.builder()
                        .id(4L)
                        .name("Перфоратор")
                        .description("Есть ударный режим, без набора сверл")
                        .available(true)
                        .build()
        ));

        assertEquals(itemsDtoTest, itemService.searchItems("М"));
        assertEquals(itemsDtoTest2, itemService.searchItems("пЕР"));
    }
}
