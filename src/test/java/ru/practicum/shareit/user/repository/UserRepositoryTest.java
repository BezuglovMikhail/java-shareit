package ru.practicum.shareit.user.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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
    User userUpdate;

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



        userUpdate = new User(
                1L,
                "nameUpdate",
                "testUpdate@mail.com"
        );

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

    @Test
    void saveAndFind() {
        userRepository.save(userSave);
        userRepository.save(userSave2);
       List<User> userList = userRepository.findAll();

       assertEquals(users, userList);
    }

    @Test
    void findUserByIdTest() {
        userRepository.save(userSave);
        userRepository.save(userSave2);
        assertEquals(Optional.of(userSave2), userRepository.findById(2L));
    }

    /*@Test
    void findAllByEmailTest() {
        userRepository.save(userSave);
        userRepository.save(userSave2);

        UserEmail userEmail = new UserEmail(
                "test@mail.com"
        );

        assertEquals(Optional.of("test@mail.com"), (UserEmail) userRepository.findAllByEmail("test@mail.com"));
    }*/
}
