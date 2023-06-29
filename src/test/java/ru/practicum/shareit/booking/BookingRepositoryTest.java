package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    private BookingRepository repository;

    @Sql({"/schema.sql"})
    @Test
    void findByBookerId() {
    }

    @Sql({"/schema.sql"})
    @Test
    void findByBookerIdAndStartIsBeforeAndEndIsAfter() {
    }

    @Sql({"/schema.sql"})
    @Test
    void findByBookerIdAndEndIsBefore() {
    }

    @Sql({"/schema.sql"})
    @Test
    void findByBookerIdAndStartIsAfter() {
    }

    @Sql({"/schema.sql"})
    @Test
    void findByBookerIdAndStatus() {
    }

    @Sql({"/schema.sql"})
    @Test
    void findByItem_Owner() {
    }

    @Sql({"/schema.sql"})
    @Test
    void findByItem_OwnerAndStartIsBeforeAndEndIsAfter() {
    }

    @Sql({"/schema.sql"})
    @Test
    void findByItem_OwnerAndEndIsBefore() {
    }

    @Sql({"/schema.sql"})
    @Test
    void findByItem_OwnerAndStartIsAfter() {
    }

    @Sql({"/schema.sql"})
    @Test
    void findByItem_OwnerAndStatus() {
    }

    @Sql({"/schema.sql"})
    @Test
    void findFirstByItem_IdAndStartBeforeAndStatusOrderByEndDesc() {
    }

    @Sql({"/schema.sql"})
    @Test
    void findFirstByItem_IdAndStartAfterAndStatusOrderByStartAsc() {
    }

    @Sql({"/schema.sql"})
    @Test
    void findFirstByItem_IdAndBooker_IdAndEndIsBeforeAndStatus() {
    }
}
