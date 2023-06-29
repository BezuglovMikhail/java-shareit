package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.Pagination;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RequestRepositoryTest {

    @Autowired
    private RequestRepository repository;

    @Autowired
    private UserRepository userRepository;

    private Request requestSave;
    private Request requestSave2;
    private Request requestSave3;
    private LocalDateTime createdRequest;
    private User user;
    private User user2;
    List<Request> requestList;

    @BeforeEach
    void setUp() {

        createdRequest = LocalDateTime.of(2023,06,29, 13, 00);

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

        requestSave = new Request(
                1L,
                "Нужна мощная штука, чтобы аккуратно собирать мебель, желательно из дерева.",
                user,
                createdRequest);

        requestSave2 = new Request(
                2L,
                "Нужна громкая штука, чтобы шуметь.",
                user,
                createdRequest.minus(Period.ofDays(1)));

        requestSave3 = new Request(
                3L,
                "Нужна громкая штука, чтобы шуметь.",
                user2,
                createdRequest.minus(Period.ofDays(2)));

        userRepository.save(user);
        userRepository.save(user2);

        repository.save(requestSave);
        repository.save(requestSave2);
        repository.save(requestSave3);
    }

    @Sql({"/schema.sql"})
    @Test
    void findAllById_Test() {
        Long requestId = 1L;
        requestList = List.of(requestSave);

        List<Request> requestListTest = repository.findAllById(requestId, Sort.by(Sort.Direction.DESC, "created"));

        assertEquals(requestList, requestListTest);
    }

    @Sql({"/schema.sql"})
    @Test
    void findAllByIdNot() {
        Long creatorRequestId = 1L;
        Integer from = 1;
        Integer size = 5;
        Pageable pageable;
        Pagination pager = new Pagination(from, size);
        Sort sort = Sort.by(Sort.Direction.DESC, "created");
        pageable = PageRequest.of(1, pager.getPageSize(), sort);
        Page<Request> pageRequest = new PageImpl(List.of(requestSave3));

        Page<Request> requestPageTest = repository.findAllByIdNot(creatorRequestId, pageable);

        assertEquals(pageRequest.getContent(), requestPageTest.getContent());

    }

    @Sql({"/schema.sql"})
    @Test
    void findAllByIdNotOrderByCreatedDesc() {
        Long creatorRequestId = 1L;
        List<Request> requestList = List.of(requestSave2, requestSave3);

        List<Request> requestListTest = repository.findAllByIdNotOrderByCreatedDesc(creatorRequestId);

        assertEquals(requestList, requestListTest);
    }
}
