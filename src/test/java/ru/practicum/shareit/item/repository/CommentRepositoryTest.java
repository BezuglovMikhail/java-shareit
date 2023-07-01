package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CommentRepositoryTest {
    @Autowired
    private CommentRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    User user;
    Item itemSave;
    Item itemSave2;
    Comment comment;
    Comment comment3;
    Comment comment2;
    List<Comment> commentList;

    LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        itemSave = new Item(
                1L,
                "?????????",
                "??????? ???",
                true,
                1L,
                null);

        itemSave2 = new Item(
                2L,
                "??????",
                "?????????? ???????",
                true,
                1L,
                1L);

        user = new User(
                1L,
                "nameTest",
                "test@mail.com"
        );

        comment = new Comment(
                1L,
                "Super, Puper!",
                itemSave,
                user,
                now.minus(Period.ofDays(1))
        );

        comment2 = new Comment(2L,
                "Puper!",
                itemSave2,
                user,
                now
        );

        userRepository.save(user);
        itemRepository.save(itemSave);
        itemRepository.save(itemSave2);
        repository.save(comment);
        repository.save(comment2);

    }

    @Test
    void findAllByItem_Id() {
        Long itemId = 1L;
        comment3 = new Comment(
                3L,
                "Super!",
                itemSave,
                user,
                now.minus(Period.ofDays(2))
        );

        repository.save(comment3);
        commentList = List.of(comment, comment3);

        List<Comment> commentListTest = repository.findAllByItem_Id(itemId,
                Sort.by(Sort.Direction.DESC, "created"));

        assertEquals(commentList, commentListTest);
    }
}
