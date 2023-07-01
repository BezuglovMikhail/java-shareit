package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.exeption.IncorrectParameterException;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    private ItemService itemService;
    @Mock
    private UserService userServiceMock;
    @Mock
    private BookingService bookingServiceMock;
    @Mock
    private ItemRepository itemRepositoryMock;
    @Mock
    private CommentRepository commentRepositoryMock;

    User user;
    UserDto userDto;
    Item itemSave;
    Item itemSave2;
    Item itemUpdate;
    List<Item> itemList;
    ItemDto itemDtoSave;
    ItemDto itemDtoSave2;
    ItemDto itemDtoFalse;
    ItemDto itemDtoUpdate;
    List<ItemDto> itemDtoList;
    BookingShortDto lastBooking;
    BookingShortDto nextBooking;
    Comment comment;
    CommentDto commentDto;
    Comment comment2;
    CommentDto commentDto2;
    List<CommentDto> commentDtoList = new ArrayList<>();
    LocalDateTime start = LocalDateTime.now().plus(Period.ofDays(1));
    LocalDateTime end = LocalDateTime.now().plus(Period.ofDays(5));
    Booking booking;

    @BeforeEach
    void setUp() {
        itemService = new ItemServiceImpl(itemRepositoryMock,
                commentRepositoryMock,
                userServiceMock,
                bookingServiceMock);

        itemSave = new Item(
                1L,
                "Колотушка",
                "Создаёт шум",
                true,
                1L,
                null);

        itemSave2 = new Item(
                2L,
                "Киянка",
                "Деревянный молоток",
                true,
                1L,
                1L);

        itemUpdate = new Item(
                1L,
                "Колотушка",
                "Почти сломана",
                false,
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
                commentDtoList);

        itemDtoSave2 = new ItemDto(
                2L,
                "Киянка",
                "Деревянный молоток",
                true,
                1L,
                1L,
                null,
                null,
                commentDtoList);

        itemDtoUpdate = new ItemDto(
                1L,
                "Колотушка",
                "Почти сломана",
                false,
                1L,
                null,
                null,
                null,
                commentDtoList);

        userDto = new UserDto(
                1L,
                "nameTest",
                "test@mail.com"
        );

        user = new User(
                1L,
                "nameTest",
                "test@mail.com"
        );

        itemList = List.of(itemSave, itemSave2);
        itemDtoList = List.of(itemDtoSave, itemDtoSave2);

        booking = new Booking(
                1L,
                start,
                end,
                itemSave,
                user,
                BookingStatus.WAITING
        );

        comment = new Comment(
                1L,
                "Супер колотушка, всем соседям понравилась!",
                itemSave,
                user,
                LocalDateTime.now().minus(Period.ofDays(1))
        );

        commentDto = new CommentDto(1L,
                "Супер колотушка, всем соседям понравилась!",
                itemSave,
                "nameTest",
                LocalDateTime.now().minus(Period.ofDays(1)));

        comment2 = new Comment(2L,
                "Очень тяжелая!!! Не рекомендую!",
                itemSave,
                user,
                LocalDateTime.now()
        );

        commentDto2 = new CommentDto(2L,
                "Очень тяжелая!!! Не рекомендую!",
                itemSave,
                "nameTest",
                LocalDateTime.now());
    }

    @Test
    void saveItem_True_Test() {
        Long ownerId = 1L;

        when(itemRepositoryMock.save(any())).thenReturn(itemSave);
        when(userServiceMock.findByIdUser(ownerId)).thenReturn(userDto);

        ItemDto itemDtoTest = itemService.save(itemDtoSave, ownerId);

        assertEquals(itemDtoSave, itemDtoTest);
        Mockito.verify(itemRepositoryMock, Mockito.times(1)).save(itemSave);
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(ownerId);
        Mockito.verifyNoMoreInteractions(itemRepositoryMock);
    }

    @Test
    void saveItem_False_Available_Test() {
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
        Mockito.verifyNoMoreInteractions(itemRepositoryMock);
    }

    @Test
    void saveItem_False_Name_Test() {
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
        Mockito.verifyNoMoreInteractions(itemRepositoryMock);
    }

    @Test
    void saveItem_False_Description_Test() {
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
        Mockito.verifyNoMoreInteractions(itemRepositoryMock);
    }

    @Test
    void saveItem_False_UserNotFound_Test() {
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
        Mockito.verify(itemRepositoryMock, Mockito.times(0)).save(itemSave);
        Mockito.verifyNoMoreInteractions(itemRepositoryMock);
    }

    @Test
    void findAllItemByIdUser_True_WhenUserId_Equals_OwnerId_Test() {
        Long userId = 1L;

        when(itemRepositoryMock.findByOwner(any())).thenReturn(itemList);
        when(itemRepositoryMock.findById(1L)).thenReturn(Optional.of(itemSave));
        when(itemRepositoryMock.findById(2L)).thenReturn(Optional.of(itemSave2));
        when(userServiceMock.findByIdUser(userId)).thenReturn(userDto);
        when(bookingServiceMock.getLastBooking(any(Long.class))).thenReturn(lastBooking);
        when(bookingServiceMock.getNextBooking(any(Long.class))).thenReturn(nextBooking);

        List<ItemDto> itemDtoListTest = itemService.findAllItemByIdUser(userId);

        assertEquals(itemDtoList, itemDtoListTest);
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verify(itemRepositoryMock, Mockito.times(1)).findByOwner(userId);
        Mockito.verify(itemRepositoryMock, Mockito.times(2)).findById(any(Long.class));
        Mockito.verifyNoMoreInteractions(itemRepositoryMock);
    }

    @Test
    void findAllItemByIdUser_True_WhenUserId_NoEquals_OwnerId_Test() {
        Long userId = 2L;

        when(itemRepositoryMock.findByOwner(any())).thenReturn(itemList);
        when(itemRepositoryMock.findById(1L)).thenReturn(Optional.of(itemSave));
        when(itemRepositoryMock.findById(2L)).thenReturn(Optional.of(itemSave2));
        when(userServiceMock.findByIdUser(userId)).thenReturn(userDto);

        List<ItemDto> itemDtoListTest = itemService.findAllItemByIdUser(userId);

        assertEquals(itemDtoList, itemDtoListTest);
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verify(itemRepositoryMock, Mockito.times(1)).findByOwner(userId);
        Mockito.verify(itemRepositoryMock, Mockito.times(2)).findById(any(Long.class));
        Mockito.verifyNoMoreInteractions(itemRepositoryMock);
    }

    @Test
    void findAllItemByIdUser_False_NotFoundUser_Test() {
        Long userId = 100L;

        when(userServiceMock.findByIdUser(userId))
                .thenThrow(new NotFoundException("User whit id = " + userId + " not found in database."));

        NotFoundException ex = assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                itemService.findAllItemByIdUser(userId);
            }
        });

        assertEquals("User whit id = " + userId + " not found in database.", ex.getMessage());
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verify(itemRepositoryMock, Mockito.times(0)).findByOwner(any());
        Mockito.verifyNoMoreInteractions(itemRepositoryMock);
    }

    @Test
    void findById_True_Test() {
        Long itemId = 1L;

        when(itemRepositoryMock.findById(itemId)).thenReturn(Optional.of(itemSave));

        ItemDto itemDtoTest = itemService.findById(itemId);

        assertEquals(itemDtoSave, itemDtoTest);
        Mockito.verify(itemRepositoryMock, Mockito.times(1)).findById(itemId);
        Mockito.verifyNoMoreInteractions(itemRepositoryMock);
    }

    @Test
    void findById_False_NotFoundItem_Test() {
        Long itemId = 10L;

        when(itemRepositoryMock.findById(itemId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                itemService.findById(itemId);
            }
        });

        assertEquals("Item whit с id = 10 not found in database.", ex.getMessage());
        Mockito.verify(itemRepositoryMock, Mockito.times(1)).findById(itemId);
        Mockito.verifyNoMoreInteractions(itemRepositoryMock);
    }

    @Test
    void findById_False_NullItemId_Test() {
        Long itemId = null;

        IncorrectParameterException ex = assertThrows(IncorrectParameterException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                itemService.findById(itemId);
            }
        });

        assertEquals("itemId", ex.getParameter());
        Mockito.verify(itemRepositoryMock, Mockito.times(0)).findById(any());
        Mockito.verifyNoMoreInteractions(itemRepositoryMock);
    }

    @Test
    void getItemById_False_NotFoundItem_Test() {
        Long userId = 5L;
        Long itemId = 10L;

        when(itemRepositoryMock.findById(itemId))
                .thenThrow(new NotFoundException("Item whit с id = " + itemId + " not found in database."));

        NotFoundException ex = assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                itemService.getItemById(itemId, userId);
            }
        });

        assertEquals("Item whit с id = 10 not found in database.", ex.getMessage());
        Mockito.verify(itemRepositoryMock, Mockito.times(1)).findById(itemId);
        Mockito.verifyNoMoreInteractions(itemRepositoryMock);
    }

    @Test
    void deleteItem_True_Test() {
        Long userId = 1L;
        Long itemId = 1L;

        when(userServiceMock.findByIdUser(userId)).thenReturn(userDto);
        itemService.deleteItem(itemId, userId);

        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verify(itemRepositoryMock, Mockito.times(1)).deleteById(itemId);
        Mockito.verifyNoMoreInteractions(itemRepositoryMock);
    }

    @Test
    void deleteItem_False_NotFoundUser_Test() {
        Long userId = 10L;
        Long itemId = 1L;

        when(userServiceMock.findByIdUser(userId))
                .thenThrow(new NotFoundException("User whit id = " + userId + " not found in database."));

        NotFoundException ex = assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                itemService.deleteItem(itemId, userId);
            }
        });

        assertEquals("User whit id = 10 not found in database.", ex.getMessage());
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verify(itemRepositoryMock, Mockito.times(0)).deleteById(any());
        Mockito.verifyNoMoreInteractions(itemRepositoryMock);
    }

    @Test
    void updateItem_True_Test() {
        Long userId = 1L;
        Long itemId = 1L;

        when(itemRepositoryMock.findById(itemId)).thenReturn(Optional.of(itemUpdate));
        when(itemRepositoryMock.save(itemUpdate)).thenReturn(itemUpdate);

        ItemDto itemDtoTest = itemService.updateItem(itemDtoUpdate, userId, itemId);

        assertEquals(itemDtoUpdate, itemDtoTest);
        Mockito.verify(itemRepositoryMock, Mockito.times(1)).findById(itemId);
        Mockito.verify(itemRepositoryMock, Mockito.times(1)).save(itemUpdate);
        Mockito.verifyNoMoreInteractions(itemRepositoryMock);
    }

    @Test
    void updateItem_False_Test() {
        Long userId = null;
        Long itemId = null;

        IncorrectParameterException ex = assertThrows(IncorrectParameterException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                itemService.updateItem(itemDtoUpdate, userId, itemId);
            }
        });

        assertEquals("itemId or userId", ex.getParameter());
        Mockito.verify(itemRepositoryMock, Mockito.times(0)).findById(any());
        Mockito.verify(itemRepositoryMock, Mockito.times(0)).save(any());
        Mockito.verifyNoMoreInteractions(itemRepositoryMock);
    }

    @Test
    void searchItems_True_EmptyList_WhenTextNull_Test() {

        String searchText = "";

        List<ItemDto> emptyList = new ArrayList<>();

        List<ItemDto> itemDtoListTest = itemService.searchItems(searchText);

        assertEquals(emptyList, itemDtoListTest);
        Mockito.verify(itemRepositoryMock, Mockito.times(0)).searchItems(any());
        Mockito.verifyNoMoreInteractions(itemRepositoryMock);
    }

    @Test
    void searchItems_True_Test() {
        String searchText = "ЛО";

        when(itemRepositoryMock.searchItems("ло")).thenReturn(itemList);

        List<ItemDto> itemDtoListTest = itemService.searchItems(searchText);

        assertEquals(itemDtoList, itemDtoListTest);
        Mockito.verify(itemRepositoryMock, Mockito.times(1)).searchItems(any());
        Mockito.verifyNoMoreInteractions(itemRepositoryMock);
    }

    @Test
    void saveComment_True_Test() {
        Long userId = 1L;
        Long itemId = 1L;

        when(bookingServiceMock.getBookingWithUserBookedItem(itemId, userId)).thenReturn(booking);
        when(userServiceMock.findByIdUser(userId)).thenReturn(userDto);
        when(commentRepositoryMock.save(any())).thenReturn(comment);

        CommentDto commentTest = itemService.saveComment(commentDto, itemId, userId);

        assertEquals(commentDto, commentTest);
        Mockito.verify(bookingServiceMock, Mockito.times(1))
                .getBookingWithUserBookedItem(itemId, userId);
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verifyNoMoreInteractions(commentRepositoryMock);
    }

    @Test
    void saveComment_False_UserNotFound_Test() {
        Long userId = 10L;
        Long itemId = 1L;

        when(userServiceMock.findByIdUser(userId))
                .thenThrow(new NotFoundException("User whit id = " + userId + " not found in database."));

        NotFoundException ex = assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                itemService.saveComment(commentDto, itemId, userId);
            }
        });

        assertEquals("User whit id = 10 not found in database.", ex.getMessage());
        Mockito.verify(bookingServiceMock, Mockito.times(0))
                .getBookingWithUserBookedItem(any(), any());
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verifyNoMoreInteractions(commentRepositoryMock);
    }

    @Test
    void saveComment_False_CommentEmpty_Test() {
        Long userId = 1L;
        Long itemId = 1L;

        CommentDto commentTextEmpty = new CommentDto(1L,
                " ",
                itemSave,
                "nameTest",
                LocalDateTime.now());

        IncorrectParameterException ex = assertThrows(IncorrectParameterException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                itemService.saveComment(commentTextEmpty, itemId, userId);
            }
        });

        assertEquals("text", ex.getParameter());
        Mockito.verify(bookingServiceMock, Mockito.times(0))
                .getBookingWithUserBookedItem(any(), any());
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verifyNoMoreInteractions(commentRepositoryMock);
    }

    @Test
    void saveComment_False_UserNotUseItem_Test() {
        Long userId = 1000L;
        Long itemId = 1L;

        when(bookingServiceMock.getBookingWithUserBookedItem(itemId, userId))
                .thenThrow(new IncorrectParameterException("userId"));
        when(userServiceMock.findByIdUser(userId)).thenReturn(userDto);

        IncorrectParameterException ex = assertThrows(IncorrectParameterException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                itemService.saveComment(commentDto, itemId, userId);
            }
        });

        assertEquals("userId", ex.getParameter());
        Mockito.verify(bookingServiceMock, Mockito.times(1))
                .getBookingWithUserBookedItem(itemId, userId);
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verifyNoMoreInteractions(commentRepositoryMock);
    }

    @Test
    void getCommentsByItemId_True_Test() {
        Long itemId = 1L;
        List<Comment> commentList = List.of(comment, comment2);
        List<CommentDto> commentDtoListTest = List.of(commentDto, commentDto2);

        when(commentRepositoryMock.findAllByItem_Id(any(), any())).thenReturn(commentList);

        List<CommentDto> commentTest = itemService.getCommentsByItemId(itemId);

        assertEquals(commentDtoListTest, commentTest);
        Mockito.verify(commentRepositoryMock, Mockito.times(1))
                .findAllByItem_Id(itemId, Sort.by(Sort.Direction.DESC, "created"));
        Mockito.verifyNoMoreInteractions(commentRepositoryMock);
    }

    @Test
    void getItemsByRequestId_True_Test() {
        Long requestId = 1L;
        Long itemId_1 = 1L;
        Long itemId_2 = 2L;

        when(itemRepositoryMock.findAllByRequestId(any(), any())).thenReturn(itemList);
        when(itemService.getCommentsByItemId(itemId_1)).thenReturn(commentDtoList);
        when(itemService.getCommentsByItemId(itemId_2)).thenReturn(commentDtoList);

        List<ItemDto> itemDtoListTest = itemService.getItemsByRequestId(requestId);

        assertEquals(itemDtoList, itemDtoListTest);
        Mockito.verify(itemRepositoryMock, Mockito.times(1))
                .findAllByRequestId(requestId, Sort.by(Sort.Direction.DESC, "id"));
        Mockito.verifyNoMoreInteractions(commentRepositoryMock);
    }
}
