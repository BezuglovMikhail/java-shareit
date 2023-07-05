package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;
    Item itemSave;
    Item itemSave2;
    Item itemSave3;
    List<Item> itemList;

    @BeforeEach
    void setUp() {
        itemSave = new Item(
                1L,
                "ItemTestNew1",
                "Description3",
                true,
                1L,
                null);

        itemSave2 = new Item(
                2L,
                "ItemTestNew2",
                "Description2",
                true,
                2L,
                1L);

        itemRepository.save(itemSave);
        itemRepository.save(itemSave2);
    }

    @Sql({"/schema.sql"})
    @Test
    void findByOwner() {
        Long ownerId = 1L;
        itemSave3 = new Item(
                3L,
                "ItemTestNew",
                "Description3",
                true,
                1L,
                null);

        itemRepository.save(itemSave3);
        itemList = List.of(itemSave, itemSave3);
        List<Item> itemsTest = itemRepository.findByOwner(ownerId);

        assertEquals(itemList, itemsTest);
    }

    @Sql({"/schema.sql"})
    @Test
    void searchItems() {
        String textSearch = "3";
        itemSave3 = new Item(
                3L,
                "ItemTestNew",
                "Description3",
                false,
                1L,
                2L);

        itemRepository.save(itemSave3);
        itemList = List.of(itemSave);
        List<Item> itemsTest = itemRepository.searchItems(textSearch);

        assertEquals(itemList, itemsTest);
    }

    @Sql({"/schema.sql"})
    @Test
    void findAllByRequestId() {
        Long requestId = 1L;
        itemSave3 = new Item(
                3L,
                "ItemTestNew",
                "Description3",
                true,
                1L,
                1L);

        itemRepository.save(itemSave3);
        itemList = List.of(itemSave3, itemSave2);
        List<Item> itemsTest = itemRepository.findAllByRequestId(requestId, Sort.by(Sort.Direction.DESC, "id"));

        assertEquals(itemList, itemsTest);
    }
}
