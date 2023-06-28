package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    private static final String USER_ID = "X-Sharer-User-Id";

    BookingDto bookingDto;

    ItemDto itemDto;

    UserDto userDto;

    BookingDto bookingDtoUpdate;

    List<BookingDto> bookingDtoList;

    LocalDateTime start = LocalDateTime.of(2023, 6, 21, 12, 00, 0);

    LocalDateTime end = LocalDateTime.of(2023, 6, 25, 12, 00, 0);

    BookingInputDto bookingInputDto = new BookingInputDto(
            1L,
            start,
            end);

    @BeforeEach
    void setUp() {

        itemDto = new ItemDto(1L,
                "Дрель-тест",
                "Дрель на ручной тяге, раритет!",
                false,
                2L,
                null,
                null,
                null,
                null
        );

        userDto = new UserDto(
                1L,
                "nameTest",
                "test@mail.com"
        );

        bookingDto = new BookingDto(1L, start, end, itemDto, userDto, BookingStatus.WAITING);

        bookingDtoUpdate = new BookingDto(1L, start, end, itemDto, userDto, BookingStatus.APPROVED);

        bookingDtoList = List.of(
                new BookingDto(1L, start, end, itemDto, userDto, BookingStatus.WAITING),
                new BookingDto(2L, start, end, itemDto, userDto, BookingStatus.APPROVED),
                new BookingDto(3L, start, end, itemDto, userDto, BookingStatus.REJECTED),
                new BookingDto(4L, start, end, itemDto, userDto, BookingStatus.CANCELED)
        );
    }

    @Test
    void createBookingTest() throws Exception {

        Long userIdTest = 1L;

        when(bookingService.save(any(), any(Long.class)))
                .thenReturn(bookingDto);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingInputDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, userIdTest))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.start",
                        is(bookingDto.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.end",
                        is(bookingDto.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.item", is(bookingDto.getItem()), ItemDto.class))
                .andExpect(jsonPath("$.booker", is(bookingDto.getBooker()), UserDto.class))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString()), BookingStatus.class));

        Mockito.verify(bookingService, Mockito.times(1))
                .save(bookingInputDto, userIdTest);

        Mockito.verifyNoMoreInteractions(bookingService);
    }

    @Test
    void updateBookingTest() throws Exception {

        Long userIdTest = 1L;
        Long bookingId = 1L;
        boolean approved = true;

        when(bookingService.update(any(Long.class), any(Long.class), any(Boolean.class)))
                .thenReturn(bookingDtoUpdate);

        mvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .content(mapper.writeValueAsString(bookingDtoUpdate))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, userIdTest)
                        .queryParam("approved", String.valueOf(approved)))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDtoUpdate.getId()), Long.class))
                .andExpect(jsonPath("$.start",
                        is(bookingDtoUpdate.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.end",
                        is(bookingDtoUpdate.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.item", is(bookingDtoUpdate.getItem()), ItemDto.class))
                .andExpect(jsonPath("$.booker", is(bookingDtoUpdate.getBooker()), UserDto.class))
                .andExpect(jsonPath("$.status", is(bookingDtoUpdate.getStatus().toString()), BookingStatus.class));

        Mockito.verify(bookingService, Mockito.times(1))
                .update(bookingId, userIdTest, approved);

        Mockito.verifyNoMoreInteractions(bookingService);
    }

    @Test
    void getBookingByIdBookingTest() throws Exception {

        Long userIdTest = 1L;
        Long bookingId = 1L;

        when(bookingService.getBookingById(any(Long.class), any(Long.class)))
                .thenReturn(bookingDto);

        mvc.perform(get("/bookings/{bookingId}", bookingId)
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, userIdTest)
                )

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.start",
                        is(bookingDto.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.end",
                        is(bookingDto.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.item", is(bookingDto.getItem()), ItemDto.class))
                .andExpect(jsonPath("$.booker", is(bookingDto.getBooker()), UserDto.class))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString()), BookingStatus.class));

        Mockito.verify(bookingService, Mockito.times(1))
                .getBookingById(bookingId, userIdTest);

        Mockito.verifyNoMoreInteractions(bookingService);
    }

    @Test
    void getBookingsTest() throws Exception {

        Long userIdTest = 1L;
        String state = "ALL";
        Integer from = 0;
        Integer size = 0;


        when(bookingService
                .getBookings(
                        any(String.class),
                        any(Long.class),
                        any(Integer.class),
                        nullable(Integer.class)))
                .thenReturn(bookingDtoList);

        mvc.perform(get("/bookings")
                        .content(mapper.writeValueAsString(bookingDtoList))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, userIdTest)
                )

                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingDtoList)));

        List<BookingDto> bookingDtosTestList = bookingService.getBookings(state, userIdTest, from, size);

        assertEquals(bookingDtoList, bookingDtosTestList);
    }

    @Test
    void getBookingsOwnerTest() throws Exception {

        Long userIdTest = 1L;
        String state = "WAITING";
        Integer from = 0;
        Integer size = 10;


        when(bookingService
                .getBookingsOwner(
                        any(String.class),
                        any(Long.class),
                        any(Integer.class),
                        nullable(Integer.class)))
                .thenReturn(bookingDtoList);

        mvc.perform(get("/bookings/owner?from={from}&size={size}", from, size)
                        .content(mapper.writeValueAsString(bookingDtoList))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, userIdTest)
                        .queryParam("state", state)
                )

                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingDtoList)));

        List<BookingDto> bookingDtosTestList = bookingService.getBookingsOwner(state, userIdTest, from, size);

        assertEquals(bookingDtoList, bookingDtosTestList);

        Mockito.verify(bookingService, Mockito.times(2))
                .getBookingsOwner(state, userIdTest, from, size);

        Mockito.verifyNoMoreInteractions(bookingService);
    }
}
