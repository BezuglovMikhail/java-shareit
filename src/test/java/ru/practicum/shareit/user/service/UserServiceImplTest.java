package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.shareit.exeption.IncorrectParameterException;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.user.dto.UserMapper.toUser;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private UserService userService;
    @Mock
    private UserRepository userRepositoryMock;
    private UserDto userDto;
    private UserDto userDtoFalse;
    private User userSave;
    private List<User> users;
    private UserDto userDtoUpdate;
    private List<UserDto> usersDto;
    private User userUpdate;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepositoryMock);

        userSave = new User(
                1L,
                "nameTest",
                "test@mail.com"
        );

        userUpdate = new User(
                1L,
                "nameUpdate",
                "testUpdate@mail.com"
        );

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

        usersDto = List.of(
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

        users = List.of(new User(
                        1L,
                        "nameTest",
                        "test@mail.com"),
                new User(
                        2L,
                        "name2Test",
                        "test2@mail.com"),
                new User(
                        3L,
                        "name3Test",
                        "test3@mail.com")
        );
    }

    @Test
    void saveUserTest() {
        when(userRepositoryMock.save(any())).thenReturn(userSave);

        UserDto userDtoSave = userService.save(userDto);

        assertEquals(userDto, userDtoSave);
        Mockito.verify(userRepositoryMock, Mockito.times(1))
               .save(userSave);
        Mockito.verifyNoMoreInteractions(userRepositoryMock);
    }

    @Test
    void saveUser_FalseEmail_Test() {

        userDtoFalse = new UserDto(
                1L,
                "nameTest",
                "testmail.com"
        );

        IncorrectParameterException ex = assertThrows(IncorrectParameterException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                userService.save(userDtoFalse);
            }
        });

        Assertions.assertEquals("email", ex.getParameter());
        Mockito.verify(userRepositoryMock, Mockito.times(0))
                .save(toUser(userDtoFalse));
        Mockito.verifyNoMoreInteractions(userRepositoryMock);
    }

    @Test
    void saveUser_NullEmail_Test() {

        userDtoFalse = new UserDto(
                1L,
                "nameTest",
                null
        );

        IncorrectParameterException ex = assertThrows(IncorrectParameterException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                userService.save(userDtoFalse);
            }
        });

        Assertions.assertEquals("email", ex.getParameter());
        Mockito.verify(userRepositoryMock, Mockito.times(0))
                .save(toUser(userDtoFalse));
        Mockito.verifyNoMoreInteractions(userRepositoryMock);
    }

    @Test
    void saveUser_RepeatEmail_Test() {

        userDtoFalse = new UserDto(
                1L,
                "nameTest",
                "test@mail.com"
        );

        when(userRepositoryMock.save(any()))
                .thenThrow(new DataIntegrityViolationException(""));

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> userService.save(userDto));

        Assertions.assertEquals("That user`s email = " + userDto.getEmail() + " exists in database!",
                exception.getMessage());
        Mockito.verify(userRepositoryMock, Mockito.times(1))
                .save(toUser(userDto));
        Mockito.verifyNoMoreInteractions(userRepositoryMock);
    }

    @Test
    void findAllUsers() {

        when(userRepositoryMock.findAll()).thenReturn(users);

        List<UserDto> usersDtoFind = userService.findAllUsers();

        assertEquals(usersDto, usersDtoFind);
        Mockito.verify(userRepositoryMock, Mockito.times(1))
                .findAll();
        Mockito.verifyNoMoreInteractions(userRepositoryMock);
    }

    @Test
    void findByIdUserTrueTest() {

        Long userId = 1L;

        when(userRepositoryMock.findById(any(Long.class))).thenReturn(Optional.of(userSave));

        UserDto userDtoFind = userService.findByIdUser(userId);

        assertEquals(userDto, userDtoFind);

        Mockito.verify(userRepositoryMock, Mockito.times(1))
                .findById(userId);
        Mockito.verifyNoMoreInteractions(userRepositoryMock);
    }

    @Test
    void findByIdUserFalseTest() {

        Long userId = 100L;

        when(userRepositoryMock.findById(userId))
                .thenThrow(new NotFoundException("User whit id = " + userId + " not found in database."));

        NotFoundException ex = assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                userService.findByIdUser(userId);
            }
        });

        Assertions.assertEquals("User whit id = 100 not found in database.", ex.getMessage());
        Mockito.verify(userRepositoryMock, Mockito.times(1))
                .findById(userId);
        Mockito.verifyNoMoreInteractions(userRepositoryMock);
    }

    @Test
    void deleteUserTrueTest() {
        Long userId = 1L;

        when(userRepositoryMock.findById(any(Long.class))).thenReturn(Optional.of(userSave));

        userService.deleteUser(userId);

        Mockito.verify(userRepositoryMock, Mockito.times(1))
                .findById(userId);
        Mockito.verify(userRepositoryMock, Mockito.times(1))
                .deleteById(userId);
        Mockito.verifyNoMoreInteractions(userRepositoryMock);
    }

    @Test
    void deleteUserFalse_NotFind_User_Test() {
        Long userId = 100L;

        when(userRepositoryMock.findById(userId))
                .thenThrow(new NotFoundException("User whit id = " + userId + " not found in database."));

        NotFoundException ex = assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                userService.deleteUser(userId);
            }
        });

        Assertions.assertEquals("User whit id = 100 not found in database.", ex.getMessage());
        Mockito.verify(userRepositoryMock, Mockito.times(1)).findById(userId);
        Mockito.verify(userRepositoryMock, Mockito.times(0)).deleteById(userId);
        Mockito.verifyNoMoreInteractions(userRepositoryMock);
    }

    @Test
    void updateUserTrue_Test() {
        Long userId = 1L;

        when(userRepositoryMock.findById(userId)).thenReturn(Optional.of(userSave));
        when(userRepositoryMock.save(any())).thenReturn(userUpdate);

        UserDto userDtoUpdateTest = userService.updateUser(userDtoUpdate, userId);

        assertEquals(userDtoUpdate, userDtoUpdateTest);
        Mockito.verify(userRepositoryMock, Mockito.times(1)).findById(userId);
        Mockito.verify(userRepositoryMock, Mockito.times(1)).findAllByEmail(userUpdate.getEmail());
        Mockito.verify(userRepositoryMock, Mockito.times(1)).save(userUpdate);
        Mockito.verifyNoMoreInteractions(userRepositoryMock);
    }

    @Test
    void updateUserFalse_NotFound_Test() {
        Long userId = 100L;

        when(userRepositoryMock.findById(userId))
                .thenThrow(new NotFoundException("User whit id = " + userId + " not found in database."));

        NotFoundException ex = assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                userService.updateUser(userDtoUpdate, userId);
            }
        });

        Assertions.assertEquals("User whit id = 100 not found in database.", ex.getMessage());
        Mockito.verify(userRepositoryMock, Mockito.times(1)).findById(userId);
        Mockito.verify(userRepositoryMock, Mockito.times(0)).save(userUpdate);
        Mockito.verifyNoMoreInteractions(userRepositoryMock);
    }

    @Test
    void updateUserFalse_RepeatEmail_Test() {
        Long userId = 1L;

        when(userRepositoryMock.findById(userId)).thenReturn(Optional.of(userSave));
        when(userRepositoryMock.findAllByEmail(userDtoUpdate.getEmail()))
                .thenThrow(new ValidationException("That user`s email = " +
                        userDtoUpdate.getEmail() + " exists in database!"));
        ValidationException ex = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                userService.updateUser(userDtoUpdate, userId);
            }
        });

        Assertions.assertEquals("That user`s email = " + userDtoUpdate.getEmail() +
                " exists in database!", ex.getMessage());
        Mockito.verify(userRepositoryMock, Mockito.times(1)).findById(userId);
        Mockito.verify(userRepositoryMock, Mockito.times(0)).save(userUpdate);
        Mockito.verifyNoMoreInteractions(userRepositoryMock);
    }
}
