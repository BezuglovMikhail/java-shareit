package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exeption.IncorrectParameterException;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.exeption.ValidationException;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceTest {

    private final UserService userService;

    @AfterEach
    void clear() {
        userService.getUserRepository().getUsers().clear();
        userService.getUserRepository().setIdUser(new AtomicLong(0));
    }

    @Test
    void saveTrue() {
        UserDto userDtoTest = UserDto.builder()
                .email("test1@mail.com")
                .name("name1")
                .build();
        UserDto userDto = userService.save(userDtoTest);
        assertNotNull(userDto);
        assertEquals(1, userDto.getId());
        assertEquals(userDto, userService.findByIdUser(1L));
    }

    @Test
    void saveFalse() {

        UserDto userDtoTest = UserDto.builder()
                .email("test2@mail.com")
                .name("name2")
                .build();

        UserDto userDtoTest1 = UserDto.builder()
                .email("test2mail.com")
                .name("name2")
                .build();

        UserDto userDtoTest2 = UserDto.builder()
                .name("name2")
                .build();

        UserDto userDtoTest3 = UserDto.builder()
                .email("test2@mail.com")
                .name("name36")
                .build();

        userService.save(userDtoTest);

        IncorrectParameterException ex = assertThrows(IncorrectParameterException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                userService.save(userDtoTest1);
            }
        });

        assertEquals("email", ex.getParameter());

        IncorrectParameterException ex2 = assertThrows(IncorrectParameterException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                userService.save(userDtoTest2);
            }
        });

        assertEquals("email", ex2.getParameter());

        ValidationException ex3 = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                userService.save(userDtoTest3);
            }
        });

        assertEquals("Пользователь с email = " + userDtoTest3.getEmail() + " уже существует.", ex3.getMessage());
    }

    @Test
    void findByIdUser() {
        NotFoundException ex = assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                userService.findByIdUser(1000);
            }
        });

        assertEquals("Пользователя с id = " + 1000 + " не существует.", ex.getMessage());
    }

    @Test
    void deleteUser() {
        NotFoundException ex = assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                userService.deleteUser(1000);
            }
        });

        assertEquals("Пользователя с id = " + 1000 + " не существует.", ex.getMessage());
    }

    @Test
    void updateUser() {

        UserDto userDtoTest = UserDto.builder()
                .email("test3@mail.com")
                .name("name3")
                .build();

        UserDto userDtoTest1 = UserDto.builder()
                .email("test4@mail.com")
                .name("name4")
                .build();

        UserDto userDtoTest2 = UserDto.builder()
                .email("test4@mail.com")
                .name("name36")
                .build();

        userService.save(userDtoTest);
        userService.save(userDtoTest1);

        ValidationException ex = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                userService.updateUser(userDtoTest2, 1);
            }
        });

        assertEquals("Пользователь с email = " + userDtoTest2.getEmail() + " уже существует.", ex.getMessage());

        NotFoundException ex2 = assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                userService.updateUser(userDtoTest2, 1000);
            }
        });

        assertEquals("Пользователя с id = " + 1000 + " не существует.", ex2.getMessage());
    }
}
