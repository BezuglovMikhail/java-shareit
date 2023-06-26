package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Lazy;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.exeption.IncorrectParameterException;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    private ItemService itemService;

    @Mock
    private UserService userServiceMock;

    private BookingService bookingService;
    @Mock
    private ItemMapper mapperMock;

    @Mock
    private ItemRepository itemRepositoryMock;

    @Mock
    private CommentRepository commentRepositoryMock;

    UserDto userDto;

    Item itemSave;

    Item itemUpdate;

    ItemDto itemDtoSave;

    ItemDto itemDtoFalse;

    ItemDto itemDtoUpdate;

    @BeforeEach
    void setUp() {
        itemService = new ItemServiceImpl(itemRepositoryMock,
                commentRepositoryMock,
                userServiceMock,
                bookingService,
                mapperMock);

        itemSave = new Item(
                1L,
                "Колотушка",
                "Создаёт шум",
                true,
                1L,
                null);

        itemDtoSave = new ItemDto(
                1L,
                "Колотушка",
                "Создаёт шум",
                true,
                1L,
                null,
                null,
                null,
                null);

        userDto = new UserDto(
                1L,
                "nameTest",
                "test@mail.com"
        );

         /*user = new User(
                1L,
                "nameTest",
                "test@mail.com"
        );

       userUpdate = new User(
                1L,
                "nameUpdate",
                "testUpdate@mail.com"
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
        );*/
    }

    @Test
    void saveItemTest() {
        Long ownerId = 1L;

        when(itemRepositoryMock.save(any())).thenReturn(itemSave);
        when(userServiceMock.findByIdUser(ownerId)).thenReturn(userDto);
        when(mapperMock.toItem(itemDtoSave)).thenReturn(itemSave);
        when(mapperMock.toItemDto(itemSave)).thenReturn(itemDtoSave);

        ItemDto itemDtoTest = itemService.save(itemDtoSave, ownerId);

        assertEquals(itemDtoSave, itemDtoTest);
        Mockito.verify(itemRepositoryMock, Mockito.times(1)).save(itemSave);
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(ownerId);
        Mockito.verify(mapperMock, Mockito.times(1)).toItem(itemDtoSave);
        Mockito.verify(itemRepositoryMock, Mockito.times(1)).save(itemSave);
        Mockito.verify(mapperMock, Mockito.times(1)).toItemDto(itemSave);
        Mockito.verifyNoMoreInteractions(itemRepositoryMock);
    }

    @Test
    void saveItemFalse_Available_Test() {
        Long ownerId = 1L;

        itemDtoFalse = new ItemDto(
                1L,
                "Колотушка",
                "Создаёт шум",
                null,
                1L,
                null,
                null,
                null,
                null);

        IncorrectParameterException ex = assertThrows(IncorrectParameterException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                itemService.save(itemDtoFalse, ownerId);
            }
        });

        assertEquals("available", ex.getParameter());
        Mockito.verify(itemRepositoryMock, Mockito.times(0)).save(itemSave);
        Mockito.verify(userServiceMock, Mockito.times(0)).findByIdUser(ownerId);
        Mockito.verify(mapperMock, Mockito.times(0)).toItem(itemDtoSave);
        Mockito.verify(itemRepositoryMock, Mockito.times(0)).save(itemSave);
        Mockito.verify(mapperMock, Mockito.times(0)).toItemDto(itemSave);
        Mockito.verifyNoMoreInteractions(itemRepositoryMock);
    }

    @Test
    void saveItemFalse_Name_Test() {
        Long ownerId = 1L;

        itemDtoFalse = new ItemDto(
                1L,
                null,
                "Создаёт шум",
                true,
                1L,
                null,
                null,
                null,
                null);

        IncorrectParameterException ex = assertThrows(IncorrectParameterException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                itemService.save(itemDtoFalse, ownerId);
            }
        });

        assertEquals("name", ex.getParameter());
        Mockito.verify(itemRepositoryMock, Mockito.times(0)).save(itemSave);
        Mockito.verify(userServiceMock, Mockito.times(0)).findByIdUser(ownerId);
        Mockito.verify(mapperMock, Mockito.times(0)).toItem(itemDtoSave);
        Mockito.verify(itemRepositoryMock, Mockito.times(0)).save(itemSave);
        Mockito.verify(mapperMock, Mockito.times(0)).toItemDto(itemSave);
        Mockito.verifyNoMoreInteractions(itemRepositoryMock);
    }

    @Test
    void saveItemFalse_Description_Test() {
        Long ownerId = 1L;

        itemDtoFalse = new ItemDto(
                1L,
                "Колотушка",
                null,
                true,
                1L,
                null,
                null,
                null,
                null);

        IncorrectParameterException ex = assertThrows(IncorrectParameterException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                itemService.save(itemDtoFalse, ownerId);
            }
        });

        assertEquals("description", ex.getParameter());
        Mockito.verify(itemRepositoryMock, Mockito.times(0)).save(itemSave);
        Mockito.verify(userServiceMock, Mockito.times(0)).findByIdUser(ownerId);
        Mockito.verify(mapperMock, Mockito.times(0)).toItem(itemDtoSave);
        Mockito.verify(itemRepositoryMock, Mockito.times(0)).save(itemSave);
        Mockito.verify(mapperMock, Mockito.times(0)).toItemDto(itemSave);
        Mockito.verifyNoMoreInteractions(itemRepositoryMock);
    }

    @Test
    void saveItemFalse_UserNotFound_Test() {
        Long ownerId = 100L;

        when(userServiceMock.findByIdUser(ownerId))
                .thenThrow(new NotFoundException("User whit id = " + ownerId + " not found in database."));

        NotFoundException ex = assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                itemService.save(itemDtoSave, ownerId);
            }
        });

        assertEquals("User whit id = " + ownerId + " not found in database.", ex.getMessage());
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(ownerId);
        Mockito.verify(mapperMock, Mockito.times(0)).toItem(itemDtoSave);
        Mockito.verify(itemRepositoryMock, Mockito.times(0)).save(itemSave);
        Mockito.verify(mapperMock, Mockito.times(0)).toItemDto(itemSave);
        Mockito.verifyNoMoreInteractions(itemRepositoryMock);
    }

    @Test
    void findAllItemByIdUser() {


    }

    @Test
    void findById() {
    }

    @Test
    void getItemById() {
    }

    @Test
    void deleteItem() {
    }

    @Test
    void updateItem() {
    }

    @Test
    void searchItems() {
    }

    @Test
    void saveComment() {
    }

    @Test
    void getCommentsByItemId() {
    }

    @Test
    void validatorCreateComment() {
    }

    @Test
    void createComment() {
    }

    @Test
    void getItemsByRequestId() {
    }
}