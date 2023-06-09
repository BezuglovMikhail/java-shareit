package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import ru.practicum.shareit.Pagination;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.exeption.IncorrectParameterException;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.booking.BookingStatus.REJECTED;
import static ru.practicum.shareit.booking.BookingStatus.WAITING;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    @Mock
    private BookingRepository repositoryMock;
    @Mock
    private UserService userServiceMock;
    @Mock
    private ItemService itemServiceMock;
    private BookingService bookingService;

    User user;
    UserDto userDto;
    Item item;
    ItemDto itemDto;
    ItemDto itemDto2;
    ItemDto itemDtoUpdate;
    BookingShortDto lastBooking;
    BookingShortDto nextBooking;
    List<CommentDto> commentDtoList = new ArrayList<>();
    LocalDateTime start = LocalDateTime.now().plus(Period.ofDays(1));
    LocalDateTime end = LocalDateTime.now().plus(Period.ofDays(5));
    Booking booking;
    Booking bookingSave;
    Booking bookingFalse;
    Booking bookingCansel;
    Booking bookingReject;
    Booking bookingUpdate;
    BookingInputDto bookingInputDtoSave;
    BookingInputDto bookingInputDtoFalse;
    BookingDto bookingDtoSave;
    BookingDto bookingDtoUpdate;
    BookingDto bookingDtoUpdate2;
    BookingDto bookingDtoUpdate3;

    @BeforeEach
    void setUp() {
        bookingService = new BookingServiceImpl(repositoryMock,
                userServiceMock,
                itemServiceMock);

        itemDto = new ItemDto(
                1L,
                "Колотушка",
                "Создаёт шум",
                true,
                2L,
                null,
                null,
                null,
                commentDtoList);

        item = new Item(
                1L,
                "Колотушка",
                "Создаёт шум",
                true,
                2L,
                null);

        itemDto2 = new ItemDto(
                2L,
                "Киянка",
                "Деревянный молоток",
                false,
                2L,
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

        booking = new Booking(
                1L,
                start,
                end,
                item,
                user,
                BookingStatus.WAITING);

        bookingSave = new Booking(
                null,
                start,
                end,
                item,
                user,
                BookingStatus.WAITING);

        bookingDtoSave = new BookingDto(
                1L,
                start,
                end,
                itemDto,
                userDto,
                BookingStatus.WAITING
        );

        bookingUpdate = new Booking(
                1L,
                start,
                end,
                item,
                user,
                BookingStatus.APPROVED);

        bookingDtoUpdate = new BookingDto(
                1L,
                start,
                end,
                itemDto,
                userDto,
                BookingStatus.APPROVED
        );
        bookingDtoUpdate2 = new BookingDto(
                1L,
                start,
                end,
                itemDto,
                userDto,
                BookingStatus.CANCELED
        );

        bookingDtoUpdate3 = new BookingDto(
                1L,
                start,
                end,
                itemDto,
                userDto,
                REJECTED
        );

        bookingInputDtoSave = new BookingInputDto(
                1L,
                start,
                end);

        bookingCansel = new Booking(
                1L,
                start,
                end,
                item,
                user,
                BookingStatus.CANCELED
        );

        bookingReject = new Booking(
                1L,
                start,
                end,
                item,
                user,
                REJECTED
        );
    }

    @Test
    void save_True_Test() {
        Long bookerId = 1L;
        Long itemId = 1L;

        when(repositoryMock.save(any())).thenReturn(booking);
        when(itemServiceMock.findById(itemId)).thenReturn(itemDto);
        when(userServiceMock.findByIdUser(bookerId)).thenReturn(userDto);

        BookingDto bookingDtoTest = bookingService.save(bookingInputDtoSave, bookerId);

        assertEquals(bookingDtoSave, bookingDtoTest);
        Mockito.verify(itemServiceMock, Mockito.times(1)).findById(itemId);
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(bookerId);
        Mockito.verify(repositoryMock, Mockito.times(1)).save(bookingSave);
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void save_False_OwnerEqualsBooker_Test() {
        Long bookerId = 2L;
        Long itemId = 1L;

        when(itemServiceMock.findById(itemId)).thenReturn(itemDto);
        when(userServiceMock.findByIdUser(bookerId)).thenReturn(userDto);

        NotFoundException ex = assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                bookingService.save(bookingInputDtoSave, bookerId);
            }
        });

        assertEquals("Owner can`t create booking his item!", ex.getMessage());
        Mockito.verify(itemServiceMock, Mockito.times(1)).findById(itemId);
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(bookerId);
        Mockito.verify(repositoryMock, Mockito.times(0)).save(booking);
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void save_False_NotFoundItem_Test() {
        Long bookerId = 100L;
        Long itemId = 1L;

        when(itemServiceMock.findById(itemId))
                .thenThrow(new NotFoundException("Item whit с id = " + itemId + " not found in database."));

        NotFoundException ex = assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                bookingService.save(bookingInputDtoSave, bookerId);
            }
        });

        assertEquals("Item whit с id = 1 not found in database.", ex.getMessage());
        Mockito.verify(itemServiceMock, Mockito.times(1)).findById(itemId);
        Mockito.verify(userServiceMock, Mockito.times(0)).findByIdUser(bookerId);
        Mockito.verify(repositoryMock, Mockito.times(0)).save(booking);
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void save_False_NotFoundUser_Test() {
        Long bookerId = 100L;
        Long itemId = 1L;

        when(itemServiceMock.findById(itemId)).thenReturn(itemDto);
        when(userServiceMock.findByIdUser(bookerId))
                .thenThrow(new NotFoundException("User whit id = " + bookerId + " not found in database."));

        NotFoundException ex = assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                bookingService.save(bookingInputDtoSave, bookerId);
            }
        });

        assertEquals("User whit id = 100 not found in database.", ex.getMessage());
        Mockito.verify(itemServiceMock, Mockito.times(1)).findById(itemId);
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(bookerId);
        Mockito.verify(repositoryMock, Mockito.times(0)).save(booking);
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void save_False_AvailableFalse_Test() {
        Long bookerId = 1L;
        Long itemId = 1L;

        when(itemServiceMock.findById(itemId)).thenReturn(itemDto2);
        when(userServiceMock.findByIdUser(bookerId)).thenReturn(userDto);

        IncorrectParameterException ex = assertThrows(IncorrectParameterException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                bookingService.save(bookingInputDtoSave, bookerId);
            }
        });

        assertEquals("available (that item already booking)", ex.getParameter());
        Mockito.verify(itemServiceMock, Mockito.times(1)).findById(itemId);
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(bookerId);
        Mockito.verify(repositoryMock, Mockito.times(0)).save(booking);
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void save_False_StartNull_Test() {
        Long bookerId = 1L;
        Long itemId = 1L;

        bookingInputDtoFalse = new BookingInputDto(
                1L,
                null,
                start);

        when(itemServiceMock.findById(itemId)).thenReturn(itemDto);
        when(userServiceMock.findByIdUser(bookerId)).thenReturn(userDto);

        IncorrectParameterException ex = assertThrows(IncorrectParameterException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                bookingService.save(bookingInputDtoFalse, bookerId);
            }
        });

        assertEquals("start or end", ex.getParameter());
        Mockito.verify(itemServiceMock, Mockito.times(1)).findById(itemId);
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(bookerId);
        Mockito.verify(repositoryMock, Mockito.times(0)).save(booking);
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void save_False_EndNull_Test() {
        Long bookerId = 1L;
        Long itemId = 1L;

        bookingInputDtoFalse = new BookingInputDto(
                1L,
                start,
                null);

        when(itemServiceMock.findById(itemId)).thenReturn(itemDto);
        when(userServiceMock.findByIdUser(bookerId)).thenReturn(userDto);

        IncorrectParameterException ex = assertThrows(IncorrectParameterException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                bookingService.save(bookingInputDtoFalse, bookerId);
            }
        });

        assertEquals("start or end", ex.getParameter());
        Mockito.verify(itemServiceMock, Mockito.times(1)).findById(itemId);
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(bookerId);
        Mockito.verify(repositoryMock, Mockito.times(0)).save(booking);
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void save_False_StartEqualsEnd_Test() {
        Long bookerId = 1L;
        Long itemId = 1L;

        bookingInputDtoFalse = new BookingInputDto(
                1L,
                start,
                start);

        when(itemServiceMock.findById(itemId)).thenReturn(itemDto);
        when(userServiceMock.findByIdUser(bookerId)).thenReturn(userDto);

        IncorrectParameterException ex = assertThrows(IncorrectParameterException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                bookingService.save(bookingInputDtoFalse, bookerId);
            }
        });

        assertEquals("start or end", ex.getParameter());
        Mockito.verify(itemServiceMock, Mockito.times(1)).findById(itemId);
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(bookerId);

        Mockito.verify(repositoryMock, Mockito.times(0)).save(booking);
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void save_False_StartAfterEnd_Test() {
        Long bookerId = 1L;
        Long itemId = 1L;

        bookingInputDtoFalse = new BookingInputDto(
                1L,
                end,
                start);

        when(itemServiceMock.findById(itemId)).thenReturn(itemDto);
        when(userServiceMock.findByIdUser(bookerId)).thenReturn(userDto);

        IncorrectParameterException ex = assertThrows(IncorrectParameterException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                bookingService.save(bookingInputDtoFalse, bookerId);
            }
        });

        assertEquals("start or end", ex.getParameter());
        Mockito.verify(itemServiceMock, Mockito.times(1)).findById(itemId);
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(bookerId);
        Mockito.verify(repositoryMock, Mockito.times(0)).save(booking);
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void save_FalseStartBeforeNow_Test() {
        Long bookerId = 1L;
        Long itemId = 1L;

        bookingInputDtoFalse = new BookingInputDto(
                1L,
                LocalDateTime.now().minus(Period.ofDays(10)),
                end);

        when(itemServiceMock.findById(itemId)).thenReturn(itemDto);
        when(userServiceMock.findByIdUser(bookerId)).thenReturn(userDto);

        IncorrectParameterException ex = assertThrows(IncorrectParameterException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                bookingService.save(bookingInputDtoFalse, bookerId);
            }
        });

        assertEquals("start or end", ex.getParameter());
        Mockito.verify(itemServiceMock, Mockito.times(1)).findById(itemId);
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(bookerId);
        Mockito.verify(repositoryMock, Mockito.times(0)).save(booking);
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void update_True_BookingStatus_APPROVED_Test() {
        Long userId = 2L;
        Long bookingId = 1L;
        Boolean approved = true;

        when(repositoryMock.save(any())).thenReturn(bookingUpdate);
        when(repositoryMock.findById(bookingId)).thenReturn(Optional.of(booking));
        when(userServiceMock.findByIdUser(userId)).thenReturn(userDto);

        BookingDto bookingDtoTest = bookingService.update(bookingId, userId, approved);

        assertEquals(bookingDtoUpdate, bookingDtoTest);
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verify(repositoryMock, Mockito.times(1)).save(booking);
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void update_True_BookingStatus_CANCELED_Test() {
        Long userId = 1L;
        Long bookingId = 1L;
        Boolean approved = false;

        when(repositoryMock.save(any())).thenReturn(bookingCansel);
        when(repositoryMock.findById(bookingId)).thenReturn(Optional.of(booking));
        when(userServiceMock.findByIdUser(userId)).thenReturn(userDto);

        BookingDto bookingDtoTest = bookingService.update(bookingId, userId, approved);

        assertEquals(bookingDtoUpdate2, bookingDtoTest);
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verify(repositoryMock, Mockito.times(1)).save(bookingCansel);
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void update_True_BookingStatus_REJECTED_Test() {
        Long userId = 2L;
        Long bookingId = 1L;
        Boolean approved = false;

        when(repositoryMock.save(any())).thenReturn(bookingReject);
        when(repositoryMock.findById(bookingId)).thenReturn(Optional.of(booking));
        when(userServiceMock.findByIdUser(userId)).thenReturn(userDto);

        BookingDto bookingDtoTest = bookingService.update(bookingId, userId, approved);

        assertEquals(bookingDtoUpdate3, bookingDtoTest);
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verify(repositoryMock, Mockito.times(1)).save(bookingReject);
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void update_False_NotFoundUser_Test() {
        Long userId = 100L;
        Long bookingId = 1L;
        Boolean approved = true;

        when(userServiceMock.findByIdUser(userId))
                .thenThrow(new NotFoundException("User whit id = " + userId + " not found in database."));

        NotFoundException ex = assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                bookingService.update(bookingId, userId, approved);
            }
        });

        assertEquals("User whit id = 100 not found in database.", ex.getMessage());
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verify(repositoryMock, Mockito.times(0)).save(booking);
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void update_False_NotFoundBooking_Test() {
        Long userId = 1L;
        Long bookingId = 100L;
        Boolean approved = true;

        when(repositoryMock.findById(bookingId))
                .thenThrow(new NotFoundException("Booking whit id = " + bookingId + " not found in database."));
        when(userServiceMock.findByIdUser(userId)).thenReturn(userDto);

        NotFoundException ex = assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                bookingService.update(bookingId, userId, approved);
            }
        });

        assertEquals("Booking whit id = 100 not found in database.", ex.getMessage());
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verify(repositoryMock, Mockito.times(0)).save(booking);
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void update_False_EndBeforeNow_Test() {
        Long userId = 2L;
        Long bookingId = 1L;
        Boolean approved = true;

        bookingFalse = new Booking(
                1L,
                LocalDateTime.now().minus(Period.ofDays(2)),
                LocalDateTime.now().minus(Period.ofDays(1)),
                item,
                user,
                BookingStatus.WAITING
        );

        when(repositoryMock.findById(bookingId)).thenReturn(Optional.of(bookingFalse));

        ValidationException ex = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                bookingService.update(bookingId, userId, approved);
            }
        });

        assertEquals("Time of booking is finish!", ex.getMessage());
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verify(repositoryMock, Mockito.times(0)).save(booking);
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void update_False_OwnerNotEqualsUser_Test() {
        Long userId = 1L;
        Long bookingId = 1L;
        Boolean approved = true;

        when(repositoryMock.findById(bookingId)).thenReturn(Optional.of(booking));
        when(userServiceMock.findByIdUser(userId)).thenReturn(userDto);

        NotFoundException ex = assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                bookingService.update(bookingId, userId, approved);
            }
        });

        assertEquals("User whit id = 1 hav`t this item!", ex.getMessage());
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verify(repositoryMock, Mockito.times(0)).save(booking);
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void update_False_BookingStatusREJECTED_Test() {
        Long userId = 2L;
        Long bookingId = 1L;
        Boolean approved = true;

        bookingFalse = new Booking(
                1L,
                start,
                end,
                item,
                user,
                REJECTED
        );

        when(repositoryMock.findById(bookingId)).thenReturn(Optional.of(bookingFalse));
        when(userServiceMock.findByIdUser(userId)).thenReturn(userDto);

        IncorrectParameterException ex = assertThrows(IncorrectParameterException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                bookingService.update(bookingId, userId, approved);
            }
        });

        assertEquals("BookingStatus", ex.getParameter());
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verify(repositoryMock, Mockito.times(1)).findById(any());
        Mockito.verify(repositoryMock, Mockito.times(0)).save(any());
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void getBookingById_True_BookerWatch_Test() {
        Long bookingId = 1L;
        Long userId = 1L;

        when(repositoryMock.findById(bookingId)).thenReturn(Optional.of(booking));
        when(userServiceMock.findByIdUser(userId)).thenReturn(userDto);

        BookingDto bookingDtoTest = bookingService.getBookingById(bookingId, userId);

        assertEquals(bookingDtoSave, bookingDtoTest);
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verify(repositoryMock, Mockito.times(1)).findById(bookingId);
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void getBookingById_True_OwnerItemWatch_Test() {
        Long bookingId = 1L;
        Long userId = 2L;

        when(repositoryMock.findById(bookingId)).thenReturn(Optional.of(booking));
        when(userServiceMock.findByIdUser(userId)).thenReturn(userDto);

        BookingDto bookingDtoTest = bookingService.getBookingById(bookingId, userId);

        assertEquals(bookingDtoSave, bookingDtoTest);
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verify(repositoryMock, Mockito.times(1)).findById(bookingId);
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void getBookingById_False_UserNotFound_Test() {
        Long bookingId = 1L;
        Long userId = 100L;

        when(userServiceMock.findByIdUser(userId))
                .thenThrow(new NotFoundException("User whit id = " + userId + " not found in database."));

        NotFoundException ex = assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                bookingService.getBookingById(bookingId, userId);
            }
        });

        assertEquals("User whit id = 100 not found in database.", ex.getMessage());
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verify(repositoryMock, Mockito.times(0)).findById(bookingId);
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void getBookingById_False_BookingNotFound_Test() {
        Long bookingId = 100L;
        Long userId = 1L;

        when(repositoryMock.findById(bookingId))
                .thenThrow(new NotFoundException("Booking whit id = " + bookingId + " not found in database."));
        when(userServiceMock.findByIdUser(userId)).thenReturn(userDto);

        NotFoundException ex = assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                bookingService.getBookingById(bookingId, userId);
            }
        });

        assertEquals("Booking whit id = 100 not found in database.", ex.getMessage());
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verify(repositoryMock, Mockito.times(1)).findById(bookingId);
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void getBookingById_False_UserNotHaveItem_Test() {
        Long bookingId = 1L;
        Long userId = 3L;

        when(repositoryMock.findById(bookingId)).thenReturn(Optional.of(booking));
        when(userServiceMock.findByIdUser(userId)).thenReturn(userDto);

        NotFoundException ex = assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                bookingService.getBookingById(bookingId, userId);
            }
        });

        assertEquals("User whit id = 3 hav`t this item!", ex.getMessage());
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verify(repositoryMock, Mockito.times(1)).findById(bookingId);
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void getBookings_True_SizeNull_State_ALL_Test() {
        Long userId = 1L;
        Integer from = 5;
        Integer size = null;
        String state = "ALL";
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pagination pager = new Pagination(from, size);
        Pageable pageable = PageRequest.of(1, pager.getPageSize(), sort);
        Page<Booking> bookingPage = new PageImpl(List.of(booking, bookingCansel, bookingReject));
        List<BookingDto> bookingDtoList = List.of(bookingDtoSave, bookingDtoUpdate2, bookingDtoUpdate3);

        when(repositoryMock.findByBookerId(userId, pageable)).thenReturn(bookingPage);
        when(userServiceMock.findByIdUser(userId)).thenReturn(userDto);

        List<BookingDto> bookingDtoListTest = bookingService.getBookings(state, userId, from, size);

        assertEquals(bookingDtoList, bookingDtoListTest);
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verify(repositoryMock, Mockito.times(1)).findByBookerId(userId, pageable);
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void getBookings_True_State_ALL_Test() {
        Long userId = 1L;
        Integer from = 5;
        Integer size = 10;
        String state = "ALL";
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pagination pager = new Pagination(from, size);
        Pageable pageable = PageRequest.of(1, pager.getPageSize(), sort);
        Page<Booking> bookingPage = new PageImpl(List.of(booking, bookingCansel, bookingReject));
        List<BookingDto> bookingDtoList = List.of(bookingDtoSave, bookingDtoUpdate2, bookingDtoUpdate3);

        when(repositoryMock.findByBookerId(userId, pageable)).thenReturn(bookingPage);
        when(userServiceMock.findByIdUser(userId)).thenReturn(userDto);

        List<BookingDto> bookingDtoListTest = bookingService.getBookings(state, userId, from, size);

        assertEquals(bookingDtoList, bookingDtoListTest);
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verify(repositoryMock, Mockito.times(1)).findByBookerId(userId, pageable);
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void getBookings_True_State_CURRENT_Test() {
        Long userId = 1L;
        Integer from = 5;
        Integer size = 10;
        String state = "CURRENT";
        Sort sort = Sort.by(Sort.Direction.ASC, "start");
        Pagination pager = new Pagination(from, size);
        Pageable pageable = PageRequest.of(1, pager.getPageSize(), sort);
        Page<Booking> bookingPage = new PageImpl(List.of(booking, bookingCansel, bookingReject));
        List<BookingDto> bookingDtoList = List.of(bookingDtoSave, bookingDtoUpdate2, bookingDtoUpdate3);

        when(repositoryMock.findByBookerIdAndStartIsBeforeAndEndIsAfter(any(), any(),
                any(), any())).thenReturn(bookingPage);
        when(userServiceMock.findByIdUser(userId)).thenReturn(userDto);

        List<BookingDto> bookingDtoListTest = bookingService.getBookings(state, userId, from, size);

        assertEquals(bookingDtoList, bookingDtoListTest);
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verify(repositoryMock, Mockito.times(1))
                .findByBookerIdAndStartIsBeforeAndEndIsAfter(any(), any(), any(), any());
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void getBookings_True_State_PAST_Test() {
        Long userId = 1L;
        Integer from = 5;
        Integer size = 10;
        String state = "PAST";
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pagination pager = new Pagination(from, size);
        Pageable pageable = PageRequest.of(1, pager.getPageSize(), sort);
        Page<Booking> bookingPage = new PageImpl(List.of(booking, bookingCansel, bookingReject));
        List<BookingDto> bookingDtoList = List.of(bookingDtoSave, bookingDtoUpdate2, bookingDtoUpdate3);

        when(repositoryMock.findByBookerIdAndEndIsBefore(any(), any(), any())).thenReturn(bookingPage);
        when(userServiceMock.findByIdUser(userId)).thenReturn(userDto);

        List<BookingDto> bookingDtoListTest = bookingService.getBookings(state, userId, from, size);

        assertEquals(bookingDtoList, bookingDtoListTest);
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verify(repositoryMock, Mockito.times(1))
                .findByBookerIdAndEndIsBefore(any(), any(), any());
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void getBookings_True_State_FUTURE_Test() {
        Long userId = 1L;
        Integer from = 5;
        Integer size = 10;
        String state = "FUTURE";
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pagination pager = new Pagination(from, size);
        Pageable pageable = PageRequest.of(1, pager.getPageSize(), sort);
        Page<Booking> bookingPage = new PageImpl(List.of(booking, bookingCansel, bookingReject));
        List<BookingDto> bookingDtoList = List.of(bookingDtoSave, bookingDtoUpdate2, bookingDtoUpdate3);

        when(repositoryMock.findByBookerIdAndStartIsAfter(any(), any(), any())).thenReturn(bookingPage);
        when(userServiceMock.findByIdUser(userId)).thenReturn(userDto);

        List<BookingDto> bookingDtoListTest = bookingService.getBookings(state, userId, from, size);

        assertEquals(bookingDtoList, bookingDtoListTest);
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verify(repositoryMock, Mockito.times(1))
                .findByBookerIdAndStartIsAfter(any(), any(), any());
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void getBookings_True_State_WAITING_Test() {
        Long userId = 1L;
        Integer from = 5;
        Integer size = 10;
        String state = "WAITING";
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pagination pager = new Pagination(from, size);
        Pageable pageable = PageRequest.of(1, pager.getPageSize(), sort);
        Page<Booking> bookingPage = new PageImpl(List.of(booking));
        List<BookingDto> bookingDtoList = List.of(bookingDtoSave);

        when(repositoryMock.findByBookerIdAndStatus(userId, WAITING, pageable)).thenReturn(bookingPage);
        when(userServiceMock.findByIdUser(userId)).thenReturn(userDto);

        List<BookingDto> bookingDtoListTest = bookingService.getBookings(state, userId, from, size);

        assertEquals(bookingDtoList, bookingDtoListTest);
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verify(repositoryMock, Mockito.times(1))
                .findByBookerIdAndStatus(userId, WAITING, pageable);
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void getBookings_True_State_REJECTED_Test() {
        Long userId = 1L;
        Integer from = 5;
        Integer size = 10;
        String state = "REJECTED";
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pagination pager = new Pagination(from, size);
        Pageable pageable = PageRequest.of(1, pager.getPageSize(), sort);
        Page<Booking> bookingPage = new PageImpl(List.of(bookingReject));
        List<BookingDto> bookingDtoList = List.of(bookingDtoUpdate3);

        when(repositoryMock.findByBookerIdAndStatus(userId, REJECTED, pageable)).thenReturn(bookingPage);
        when(userServiceMock.findByIdUser(userId)).thenReturn(userDto);

        List<BookingDto> bookingDtoListTest = bookingService.getBookings(state, userId, from, size);

        assertEquals(bookingDtoList, bookingDtoListTest);
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verify(repositoryMock, Mockito.times(1))
                .findByBookerIdAndStatus(userId, REJECTED, pageable);
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void getBookings_False_NotFoundUser_Test() {
        Long userId = 100L;
        Integer from = 5;
        Integer size = 10;
        String state = "ALL";

        when(userServiceMock.findByIdUser(userId))
                .thenThrow(new NotFoundException("User whit id = " + userId + " not found in database."));

        NotFoundException ex = assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                bookingService.getBookings(state, userId, from, size);
            }
        });

        assertEquals("User whit id = 100 not found in database.", ex.getMessage());
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verify(repositoryMock, Mockito.times(0)).findByBookerId(any(), any());
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void getBookings_False_IncorrectState_Test() {
        Long userId = 1L;
        Integer from = 5;
        Integer size = 10;
        String state = "LIGFGLUKFUK";

        when(userServiceMock.findByIdUser(userId)).thenReturn(userDto);

        IncorrectParameterException ex = assertThrows(IncorrectParameterException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                bookingService.getBookings(state, userId, from, size);
            }
        });

        assertEquals(state, ex.getParameter());
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verify(repositoryMock, Mockito.times(0)).findByBookerId(any(), any());
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void getBookings_False_SizeNull_IncorrectState_Test() {
        Long userId = 1L;
        Integer from = 5;
        Integer size = 10;
        String state = "LIGFGLUKFUK";

        when(userServiceMock.findByIdUser(userId)).thenReturn(userDto);

        IncorrectParameterException ex = assertThrows(IncorrectParameterException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                bookingService.getBookings(state, userId, from, size);
            }
        });

        assertEquals(state, ex.getParameter());
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verify(repositoryMock, Mockito.times(0)).findByBookerId(any(), any());
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void getBookingsOwner_SizeNull_StateAll_Test() {
        Long userId = 1L;
        Integer from = 5;
        Integer size = null;
        String state = "ALL";
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pagination pager = new Pagination(from, size);
        Pageable pageable = PageRequest.of(1, pager.getPageSize(), sort);
        Page<Booking> bookingPage = new PageImpl(List.of(booking, bookingCansel, bookingReject));
        List<BookingDto> bookingDtoList = List.of(bookingDtoSave, bookingDtoUpdate2, bookingDtoUpdate3);

        when(repositoryMock.findByItem_Owner(userId, pageable)).thenReturn(bookingPage);
        when(userServiceMock.findByIdUser(userId)).thenReturn(userDto);

        List<BookingDto> bookingDtoListTest = bookingService.getBookingsOwner(state, userId, from, size);

        assertEquals(bookingDtoList, bookingDtoListTest);
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verify(repositoryMock, Mockito.times(1)).findByItem_Owner(userId, pageable);
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void getBookingsOwner_True_State_ALL_Test() {
        Long userId = 1L;
        Integer from = 5;
        Integer size = 10;
        String state = "ALL";
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pagination pager = new Pagination(from, size);
        Pageable pageable = PageRequest.of(1, pager.getPageSize(), sort);
        Page<Booking> bookingPage = new PageImpl(List.of(booking, bookingCansel, bookingReject));
        List<BookingDto> bookingDtoList = List.of(bookingDtoSave, bookingDtoUpdate2, bookingDtoUpdate3);

        when(repositoryMock.findByItem_Owner(userId, pageable)).thenReturn(bookingPage);
        when(userServiceMock.findByIdUser(userId)).thenReturn(userDto);

        List<BookingDto> bookingDtoListTest = bookingService.getBookingsOwner(state, userId, from, size);

        assertEquals(bookingDtoList, bookingDtoListTest);
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verify(repositoryMock, Mockito.times(1)).findByItem_Owner(userId, pageable);
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void getBookingsOwner_True_State_CURRENT_Test() {
        Long userId = 1L;
        Integer from = 5;
        Integer size = 10;
        String state = "CURRENT";
        Page<Booking> bookingPage = new PageImpl(List.of(booking, bookingCansel, bookingReject));
        List<BookingDto> bookingDtoList = List.of(bookingDtoSave, bookingDtoUpdate2, bookingDtoUpdate3);

        when(repositoryMock
                .findByItem_OwnerAndStartIsBeforeAndEndIsAfter(any(), any(), any(), any())).thenReturn(bookingPage);
        when(userServiceMock.findByIdUser(userId)).thenReturn(userDto);

        List<BookingDto> bookingDtoListTest = bookingService.getBookingsOwner(state, userId, from, size);

        assertEquals(bookingDtoList, bookingDtoListTest);
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verify(repositoryMock, Mockito.times(1))
                .findByItem_OwnerAndStartIsBeforeAndEndIsAfter(any(), any(), any(), any());
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void getBookingsOwner_True_State_PAST_Test() {
        Long userId = 1L;
        Integer from = 5;
        Integer size = 10;
        String state = "PAST";
        Page<Booking> bookingPage = new PageImpl(List.of(booking, bookingCansel, bookingReject));
        List<BookingDto> bookingDtoList = List.of(bookingDtoSave, bookingDtoUpdate2, bookingDtoUpdate3);

        when(repositoryMock.findByItem_OwnerAndEndIsBefore(any(), any(), any())).thenReturn(bookingPage);
        when(userServiceMock.findByIdUser(userId)).thenReturn(userDto);

        List<BookingDto> bookingDtoListTest = bookingService.getBookingsOwner(state, userId, from, size);

        assertEquals(bookingDtoList, bookingDtoListTest);
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verify(repositoryMock, Mockito.times(1))
                .findByItem_OwnerAndEndIsBefore(any(), any(), any());
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void getBookingsOwner_True_State_FUTURE_Test() {
        Long userId = 1L;
        Integer from = 5;
        Integer size = 10;
        String state = "FUTURE";
        Page<Booking> bookingPage = new PageImpl(List.of(booking, bookingCansel, bookingReject));
        List<BookingDto> bookingDtoList = List.of(bookingDtoSave, bookingDtoUpdate2, bookingDtoUpdate3);

        when(repositoryMock.findByItem_OwnerAndStartIsAfter(any(), any(), any())).thenReturn(bookingPage);
        when(userServiceMock.findByIdUser(userId)).thenReturn(userDto);

        List<BookingDto> bookingDtoListTest = bookingService.getBookingsOwner(state, userId, from, size);

        assertEquals(bookingDtoList, bookingDtoListTest);
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verify(repositoryMock, Mockito.times(1))
                .findByItem_OwnerAndStartIsAfter(any(), any(), any());
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void getBookingsOwner_True_State_WAITING_Test() {
        Long userId = 1L;
        Integer from = 5;
        Integer size = 10;
        String state = "WAITING";
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pagination pager = new Pagination(from, size);
        Pageable pageable = PageRequest.of(1, pager.getPageSize(), sort);
        Page<Booking> bookingPage = new PageImpl(List.of(booking));
        List<BookingDto> bookingDtoList = List.of(bookingDtoSave);

        when(repositoryMock.findByItem_OwnerAndStatus(userId, WAITING, pageable)).thenReturn(bookingPage);
        when(userServiceMock.findByIdUser(userId)).thenReturn(userDto);

        List<BookingDto> bookingDtoListTest = bookingService.getBookingsOwner(state, userId, from, size);

        assertEquals(bookingDtoList, bookingDtoListTest);
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verify(repositoryMock, Mockito.times(1))
                .findByItem_OwnerAndStatus(userId, WAITING, pageable);
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void getBookingsOwner_True_State_REJECTED_Test() {
        Long userId = 1L;
        Integer from = 5;
        Integer size = 10;
        String state = "REJECTED";
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pagination pager = new Pagination(from, size);
        Pageable pageable = PageRequest.of(1, pager.getPageSize(), sort);
        Page<Booking> bookingPage = new PageImpl(List.of(bookingReject));
        List<BookingDto> bookingDtoList = List.of(bookingDtoUpdate3);

        when(repositoryMock.findByItem_OwnerAndStatus(userId, REJECTED, pageable)).thenReturn(bookingPage);
        when(userServiceMock.findByIdUser(userId)).thenReturn(userDto);

        List<BookingDto> bookingDtoListTest = bookingService.getBookingsOwner(state, userId, from, size);

        assertEquals(bookingDtoList, bookingDtoListTest);
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verify(repositoryMock, Mockito.times(1))
                .findByItem_OwnerAndStatus(userId, REJECTED, pageable);
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void getBookingsOwner_False_NotFoundUser_Test() {
        Long userId = 100L;
        Integer from = 5;
        Integer size = 10;
        String state = "ALL";

        when(userServiceMock.findByIdUser(userId))
                .thenThrow(new NotFoundException("User whit id = " + userId + " not found in database."));

        NotFoundException ex = assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                bookingService.getBookingsOwner(state, userId, from, size);
            }
        });

        assertEquals("User whit id = 100 not found in database.", ex.getMessage());
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verify(repositoryMock, Mockito.times(0)).findByItem_Owner(any(), any());
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void getBookingsOwner_False_IncorrectState_Test() {
        Long userId = 1L;
        Integer from = 5;
        Integer size = 10;
        String state = "LIGFGLUKFUK";

        when(userServiceMock.findByIdUser(userId)).thenReturn(userDto);

        IncorrectParameterException ex = assertThrows(IncorrectParameterException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                bookingService.getBookingsOwner(state, userId, from, size);
            }
        });

        assertEquals(state, ex.getParameter());
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verify(repositoryMock, Mockito.times(0)).findByItem_Owner(any(), any());
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void getBookingsOwner_False_SizeNull_IncorrectState_Test() {
        Long userId = 1L;
        Integer from = 5;
        Integer size = 10;
        String state = "LIGFGLUKFUK";

        when(userServiceMock.findByIdUser(userId)).thenReturn(userDto);

        IncorrectParameterException ex = assertThrows(IncorrectParameterException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                bookingService.getBookingsOwner(state, userId, from, size);
            }
        });

        assertEquals(state, ex.getParameter());
        Mockito.verify(userServiceMock, Mockito.times(1)).findByIdUser(userId);
        Mockito.verify(repositoryMock, Mockito.times(0)).findByItem_Owner(any(), any());
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }
}
