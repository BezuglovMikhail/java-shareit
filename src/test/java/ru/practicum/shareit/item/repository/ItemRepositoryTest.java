package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRepositoryTest {

    private final ItemRepository itemRepository;

    @BeforeEach
    void create() {
        itemRepository.save(ItemDto.builder()
                .name("Отвертка")
                .description("Шлицевая отвертка")
                .available(true)
                .build(), 1L);

        itemRepository.save(ItemDto.builder()
                .name("Отвертка")
                .description("Крестовая отвертка")
                .available(false)
                .build(), 1L);

        itemRepository.save(ItemDto.builder()
                .name("Аккумуляторный шуруповерт")
                .description("шуруповерт + аккумулятор")
                .available(true)
                .build(), 2L);
    }

    @AfterEach
    void clear() {
        itemRepository.getItems().clear();
        itemRepository.setId(new AtomicLong(0));
    }

    @Test
    void save() {

        ItemDto itemTest = ItemDto.builder()
                .name("Молоток")
                .description("Молоток с гвоздодером")
                .available(true)
                .build();

        ItemDto itemTest2 = ItemDto.builder()
                .name("Перфоратор")
                .description("Есть ударный режим и набор сверл по бетону")
                .available(false)
                .build();

        Item itemAfterSave1 = itemRepository.save(itemTest, 3L);
        Item itemAfterSave2 = itemRepository.save(itemTest2, 1L);

        assertEquals(4, itemAfterSave1.getId());
        assertEquals(5, itemAfterSave2.getId());
        assertEquals(itemAfterSave1, itemRepository.findById(4L));
        assertEquals(itemAfterSave2, itemRepository.findById(5L));
    }

    @Test
    void findAllItemsByIdUser() {
        ItemDto itemTest = ItemDto.builder()
                .name("Молоток")
                .description("Молоток с гвоздодером")
                .available(true)
                .build();

        ItemDto itemTest2 = ItemDto.builder()
                .name("Перфоратор")
                .description("Есть ударный режим и набор сверл по бетону")
                .available(false)
                .build();

        List<Item> itemsTest = new ArrayList<>(List.of(
                Item.builder()
                        .id(1L)
                        .name("Отвертка")
                        .description("Шлицевая отвертка")
                        .available(true)
                        .owner(1L)
                        .build(),
                Item.builder()
                        .id(2L)
                        .name("Отвертка")
                        .description("Крестовая отвертка")
                        .available(false)
                        .owner(1L)
                        .build(),
                Item.builder()
                        .id(5L)
                        .name("Перфоратор")
                        .description("Есть ударный режим и набор сверл по бетону")
                        .available(false)
                        .owner(1L)
                        .build()
        ));

        List<Item> itemsTest2 = new ArrayList<>(List.of(
                Item.builder()
                        .id(4L)
                        .name("Молоток")
                        .description("Молоток с гвоздодером")
                        .available(true)
                        .owner(3L)
                        .build()
        ));

        itemRepository.save(itemTest, 3L);
        itemRepository.save(itemTest2, 1L);

        assertEquals(3, itemRepository.findAllItemsByIdUser(1L).size());
        assertEquals(1, itemRepository.findAllItemsByIdUser(3L).size());
        assertEquals(itemsTest, itemRepository.findAllItemsByIdUser(1L));
        assertEquals(itemsTest2, itemRepository.findAllItemsByIdUser(3L));
    }

    @Test
    void findById() {
        ItemDto itemTest = ItemDto.builder()
                .name("Молоток")
                .description("Молоток с гвоздодером")
                .available(true)
                .build();

        ItemDto itemTest2 = ItemDto.builder()
                .name("Перфоратор")
                .description("Есть ударный режим и набор сверл по бетону")
                .available(false)
                .build();

        Item itemsTest = Item.builder()
                .id(1L)
                .name("Отвертка")
                .description("Шлицевая отвертка")
                .available(true)
                .owner(1L)
                .build();

        Item itemAfterSave1 = itemRepository.save(itemTest, 3L);
        Item itemAfterSave2 = itemRepository.save(itemTest2, 1L);

        assertEquals(itemAfterSave1, itemRepository.findById(4L));
        assertEquals(itemAfterSave2, itemRepository.findById(5L));
        assertEquals(itemsTest, itemRepository.findById(1L));
    }

    @Test
    void deleteItem() {
        ItemDto itemTest = ItemDto.builder()
                .name("Молоток")
                .description("Молоток с гвоздодером")
                .available(true)
                .build();

        ItemDto itemTest2 = ItemDto.builder()
                .name("Перфоратор")
                .description("Есть ударный режим и набор сверл по бетону")
                .available(false)
                .build();

        itemRepository.save(itemTest, 3L);
        itemRepository.save(itemTest2, 1L);

        itemRepository.deleteItem(1L, 2L);
        itemRepository.deleteItem(3L, 4L);
        assertNull(itemRepository.findById(2L));
        assertNull(itemRepository.findById(4L));
    }

    @Test
    void updateItem() {
        ItemDto itemTest = ItemDto.builder()
                .name("Молоток")
                .description("Молоток с гвоздодером")
                .available(true)
                .build();

        ItemDto itemTest2 = ItemDto.builder()
                .name("Перфоратор")
                .description("Есть ударный режим и набор сверл по бетону")
                .available(false)
                .build();

        itemRepository.save(itemTest, 3L);
        itemRepository.save(itemTest2, 1L);

        List<Item> itemsTest = new ArrayList<>(List.of(
                Item.builder()
                        .id(1L)
                        .name("Отвертка супер мощная")
                        .description("Шлицевая отвертка")
                        .available(true)
                        .owner(1L)
                        .build(),
                Item.builder()
                        .id(2L)
                        .name("Отвертка")
                        .description("Крестовая отвертка")
                        .available(true)
                        .owner(1L)
                        .build(),
                Item.builder()
                        .id(5L)
                        .name("Перфоратор+")
                        .description("Есть ударный режим, без сверл")
                        .available(true)
                        .owner(1L)
                        .build()
        ));

        itemRepository.updateItem(ItemDto.builder()
                .name("Отвертка супер мощная")
                .build(), 1L, 1L);

        itemRepository.updateItem(ItemDto.builder()
                .available(true)
                .build(), 1L, 2L);

        itemRepository.updateItem(ItemDto.builder()
                .name("Перфоратор+")
                .description("Есть ударный режим, без сверл")
                .available(true)
                .build(), 1L, 5L);

        assertEquals(itemsTest, itemRepository.findAllItemsByIdUser(1L));
    }

    @Test
    void searchItems() {
        ItemDto itemTest = ItemDto.builder()
                .name("Электрическая отвертка")
                .description("Отвертка + набор бит")
                .available(true)
                .build();

        ItemDto itemTest2 = ItemDto.builder()
                .name("Перфоратор")
                .description("Есть ударный режим, отвертка и набор сверл по бетону")
                .available(true)
                .build();

        itemRepository.save(itemTest, 3L);
        itemRepository.save(itemTest2, 1L);

        List<Item> itemsTest = new ArrayList<>(List.of(
                Item.builder()
                        .id(4L)
                        .name("Электрическая отвертка")
                        .description("Отвертка + набор бит")
                        .available(true)
                        .owner(3L)
                        .build(),

                Item.builder()
                        .id(5L)
                        .name("Перфоратор")
                        .description("Есть ударный режим, отвертка и набор сверл по бетону")
                        .available(true)
                        .owner(1L)
                        .build()
        ));

        List<Item> itemsTest2 = new ArrayList<>(List.of(
                Item.builder()
                        .id(1L)
                        .name("Отвертка")
                        .description("Шлицевая отвертка")
                        .available(true)
                        .owner(1L)
                        .build(),
                Item.builder()
                        .id(3L)
                        .name("Аккумуляторный шуруповерт")
                        .description("шуруповерт + аккумулятор")
                        .available(true)
                        .owner(2L)
                        .build(),
                Item.builder()
                        .id(4L)
                        .name("Электрическая отвертка")
                        .description("Отвертка + набор бит")
                        .available(true)
                        .owner(3L)
                        .build(),

                Item.builder()
                        .id(5L)
                        .name("Перфоратор")
                        .description("Есть ударный режим, отвертка и набор сверл по бетону")
                        .available(true)
                        .owner(1L)
                        .build()
        ));

        assertEquals(itemsTest, itemRepository.searchItems("набор"));
        assertEquals(itemsTest2, itemRepository.searchItems("верт"));
    }
}
