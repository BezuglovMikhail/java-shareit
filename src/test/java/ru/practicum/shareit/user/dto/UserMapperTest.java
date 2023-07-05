package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JsonTest
@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    private UserMapper mapper;

    User user;
    User user2;
    UserDto userDto;
    UserDto userDto2;
    List<User> userList;
    List<UserDto> userDtoList;

    @BeforeEach
    void setUp() {

        userDto = new UserDto(
                1L,
                "nameTest",
                "test@mail.com"
        );

        userDto2 = new UserDto(
                2L,
                "nameTest2",
                "test2@mail.com"
        );

        user = new User(
                1L,
                "nameTest",
                "test@mail.com"
        );

        user2 = new User(
                2L,
                "nameTest2",
                "test2@mail.com"
        );

        userList = List.of(user, user2);
        userDtoList = List.of(userDto, userDto2);
    }

    @Test
    void toUserDto() {
        UserDto userDtoTest = mapper.toUserDto(user);

        assertEquals(userDto, userDtoTest);
    }

    @Test
    void toUser() {
        User userTest = mapper.toUser(userDto);

        assertEquals(user, userTest);
    }

    @Test
    void mapToUserDto() {
        List<UserDto> userDtoListTest = mapper.mapToUserDto(userList);

        assertEquals(userDtoList, userDtoListTest);
    }
}
