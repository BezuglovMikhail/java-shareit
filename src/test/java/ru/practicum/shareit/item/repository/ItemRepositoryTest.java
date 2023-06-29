package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Sql({"/schema.sql"})
    @Test
    void findByOwner() {
    }

    @Sql({"/schema.sql"})
    @Test
    void searchItems() {
    }

    @Sql({"/schema.sql"})
    @Test
    void findAllByRequestId() {
    }
}
