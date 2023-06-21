package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserService userService;

    @Autowired
    private MockMvc mvc;

    private UserDto userDto;

    private UserDto userDtoUpdate;

    private List<UserDto> usersDto = List.of(
            new UserDto(
                    1L,
                    "nameTest",
                    "test@mail.com"),
            new UserDto(
                    2L,
                    "name2Test",
                    "test2@mail.com"),
            new UserDto(
                    3L,
                    "name3Test",
                    "test3@mail.com")
    );

    @BeforeEach
    void setUp() {

        userDto = new UserDto(
                1L,
                "nameTest",
                "test@mail.com"
        );

        userDtoUpdate = new UserDto(
                1L,
                "nameUpdate",
                "testUpdate@mail.com"
        );
    }

    @Test
    void getAllUsersTest() throws Exception {
        when(userService.findAllUsers())
                .thenReturn(usersDto);

        mvc.perform(get("/users")
                        .content(mapper.writeValueAsString(usersDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(usersDto)))
        ;

        List<UserDto> usersTestList = userService.findAllUsers();

        assertEquals(usersDto, usersTestList);

        Mockito.verify(userService, Mockito.times(2))
                .findAllUsers();

        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    void saveUserTest() throws Exception {
        when(userService.save(any()))
                .thenReturn(userDto);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));

        Mockito.verify(userService, Mockito.times(1))
                .save(userDto);

        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    void updateUserTest() throws Exception {
        Long userIdTest = 1L;

        when(userService.updateUser(any(), any(Long.class)))
                .thenReturn(userDtoUpdate);

        mvc.perform(patch("/users/{userId}", userIdTest)
                        .content(mapper.writeValueAsString(userDtoUpdate))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDtoUpdate.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDtoUpdate.getName())))
                .andExpect(jsonPath("$.email", is(userDtoUpdate.getEmail())));

        Mockito.verify(userService, Mockito.times(1))
                .updateUser(userDtoUpdate, userIdTest);

        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    void findByIdTest() throws Exception {
        Long userIdTest = 1L;

        when(userService.findByIdUser(any(Long.class)))
                .thenReturn(userDto);

        mvc.perform(get("/users/{userId}", userIdTest)
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));

        Mockito.verify(userService, Mockito.times(1))
                .findByIdUser(userIdTest);

        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    void deleteUser() throws Exception {
        Long userIdTest = 1L;

        mvc.perform(delete("/users/{userId}", userIdTest))
                .andExpect(status().isOk())
        ;

        Mockito.verify(userService, Mockito.times(1))
                .deleteUser(userIdTest);

        Mockito.verifyNoMoreInteractions(userService);
    }
}
