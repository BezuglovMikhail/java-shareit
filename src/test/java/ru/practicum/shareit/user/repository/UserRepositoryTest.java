package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserRepositoryTest {

    private final UserRepository userRepository;

    @BeforeEach
    void create() {
        userRepository.save(UserDto.builder()
                .email("test1@mail.com")
                .name("name1")
                .build());
        userRepository.save(UserDto.builder()
                .email("test2@mail.com")
                .name("name2")
                .build());
        userRepository.save(UserDto.builder()
                .email("test3@mail.com")
                .name("name3")
                .build());
    }

    @AfterEach
    void clear() {
        userRepository.getUsers().clear();
        userRepository.setIdUser(0);
    }

    @Test
    void saveTrue() {
        UserDto userDto = UserDto.builder()
                .email("test4@mail.com")
                .name("name4")
                .build();
        User user = userRepository.save(userDto);
        assertNotNull(user);
        assertEquals(4, user.getId());
        assertEquals(user, userRepository.findByIdUser(4L));
    }

    @Test
    void findAllUsers() {
        UserDto userDto = UserDto.builder()
                .email("test4@mail.com")
                .name("name4")
                .build();
        userRepository.save(userDto);

        List<User> usersTest = new ArrayList<>(List.of(User.builder()
                        .id(1)
                        .email("test1@mail.com")
                        .name("name1")
                        .build(),
                User.builder()
                        .id(2)
                        .email("test2@mail.com")
                        .name("name2")
                        .build(),
                User.builder()
                        .id(3)
                        .email("test3@mail.com")
                        .name("name3")
                        .build(),
                User.builder()
                        .id(4)
                        .email("test4@mail.com")
                        .name("name4")
                        .build()));

        List<User> users = userRepository.findAllUsers();

        assertEquals(4, users.size());
        assertEquals(usersTest, users);
    }

   @Test
    void findByIdUser() {
        UserDto userDto = UserDto.builder()
                .email("test4@mail.com")
                .name("name4")
                .build();
        userRepository.save(userDto);

        User userTest1 = User.builder()
                .id(2)
                .email("test2@mail.com")
                .name("name2")
                .build();

        User userTest2 = User.builder()
                .id(4)
                .email("test4@mail.com")
                .name("name4")
                .build();

        assertEquals(userTest1, userRepository.findByIdUser(2));
        assertEquals(userTest2, userRepository.findByIdUser(4));
    }

    @Test
    void deleteUser() {
        UserDto userDto = UserDto.builder()
                .email("test4@mail.com")
                .name("name4")
                .build();
        userRepository.save(userDto);

        List<User> usersTest = new ArrayList<>(List.of(User.builder()
                        .id(1)
                        .email("test1@mail.com")
                        .name("name1")
                        .build(),
                User.builder()
                        .id(4)
                        .email("test4@mail.com")
                        .name("name4")
                        .build()));

        userRepository.deleteUser(2);
        userRepository.deleteUser(3);
        List<User> users = userRepository.findAllUsers();

        assertEquals(2, users.size());
        assertEquals(usersTest, users);

        userRepository.deleteUser(4);
        usersTest.remove(1);
        List<User> users2 = userRepository.findAllUsers();

        assertEquals(1, users2.size());
        assertEquals(usersTest, users2);
    }

    @Test
    void updateUser() {
        UserDto userDto = UserDto.builder()
                .email("test4@mail.com")
                .name("name4")
                .build();
        userRepository.save(userDto);

        userRepository.updateUser(UserDto.builder()
                .email("test11111@mail.com")
                .build(), 1L);

        userRepository.updateUser(UserDto.builder()
                .name("name222222")
                .build(), 2L);
        userRepository.updateUser(UserDto.builder()
                .email("test44444@mail.com")
                .name("name44444")
                .build(), 4L);

        List<User> usersTest = new ArrayList<>(List.of(User.builder()
                        .id(1)
                        .email("test11111@mail.com")
                        .name("name1")
                        .build(),
                User.builder()
                        .id(2)
                        .email("test2@mail.com")
                        .name("name222222")
                        .build(),
                User.builder()
                        .id(3)
                        .email("test3@mail.com")
                        .name("name3")
                        .build(),
                User.builder()
                        .id(4)
                        .email("test44444@mail.com")
                        .name("name44444")
                        .build()));

      List<User> users = userRepository.findAllUsers();

      assertEquals(4, users.size());
      assertEquals(usersTest, users);
    }
}
