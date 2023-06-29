package ru.practicum.shareit.user.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    User userSave;

    User userSave2;

    List<User> users;

    @BeforeEach
    void setUp() {

        userSave = new User(
                1L,
                "nameTest",
                "test@mail.com"
        );

        userSave2 = new User(
                2L,
                "nameTest2",
                "test2@mail.com"
        );

        userRepository.save(userSave);
        userRepository.save(userSave2);

        users = List.of(new User(
                        1L,
                        "nameTest",
                        "test@mail.com"),
                new User(
                        2L,
                        "nameTest2",
                        "test2@mail.com")
        );
    }

    @Sql({"/schema.sql"})
    @Test
    void findAll() {
       List<User> userList = userRepository.findAll();

       assertEquals(users, userList);
    }

    @Sql({"/schema.sql"})
    @Test
    void findUserByIdTest() {
       User userSave3 = new User(
                3L,
                "nameTest3",
                "test3@mail.com");

        userRepository.save(userSave3);
        assertEquals(Optional.of(userSave3), userRepository.findById(3L));
    }
}
