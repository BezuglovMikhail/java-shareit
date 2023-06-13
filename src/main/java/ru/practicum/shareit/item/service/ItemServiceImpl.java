package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.exeption.IncorrectParameterException;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static ru.practicum.shareit.validator.Validator.*;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {

    @Autowired
    private final ItemRepository itemRepository;
    @Autowired
    private final CommentRepository commentRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final BookingService bookingService;
    @Autowired
    private final ItemMapper mapper;

    public ItemServiceImpl(ItemRepository itemRepository,
                           CommentRepository commentRepository,
                           UserRepository userRepository,
                           BookingService bookingService,
                           ItemMapper mapper) {
        this.itemRepository = itemRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.bookingService = bookingService;
        this.mapper = mapper;
    }

    @Override
    public ItemDto save(ItemDto itemDto, Long userId) {
        validatorItem(itemDto);
        validatorUserId(userRepository.findById(userId), userId);
        itemDto.setOwner(userId);
        Item item = itemRepository.save(mapper.toItem(itemDto));
        return mapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> findAllItemByIdUser(Long userId) {
        validatorUserId(userRepository.findById(userId), userId);
        List<Item> items = itemRepository.findByOwner(userId);
        return items.stream()
                .map(mapper::toItemExtDto)
                .sorted(Comparator.comparing(ItemDto::getId))
                .collect(toList());
    }

    @Override
    public ItemDto findById(Long itemId) {
        if (itemId != null) {
            Optional<Item> item = itemRepository.findById(itemId);
            if (item.isPresent()) {
                return mapper.toItemDto(item.get());
            } else {
                throw new NotFoundException("Вещь с id = " + itemId + " не найдена.");
            }
        } else {
            throw new IncorrectParameterException("itemId или userId");
        }
    }

    @Override
    public Item findItemById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Вещь с id = " + id + " не найдена!"));
    }

    @Override
    public ItemDto getItemById(Long id, Long userId) {
        ItemDto itemDto;
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Вещь с id = " + id + " не найдена!"));
        if (userId.equals(item.getOwner())) {
            itemDto = mapper.toItemExtDto(item);
        } else {
            itemDto = mapper.toItemDto(item);
        }
        return itemDto;
    }


    @Override
    public void deleteItem(Long itemId, Long userId) {
        validatorUserId(userRepository.findById(userId), userId);
        itemRepository.deleteById(itemId);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long userId, Long itemId) {
        if (itemId != null & userId != null) {
            ItemDto itemUpdate = findById(itemId);
            validatorItemIdByUserId(itemUpdate, userId);
            if (itemDto.getName() != null) {
                itemUpdate.setName(itemDto.getName());
            }
            if (itemDto.getDescription() != null) {
                itemUpdate.setDescription(itemDto.getDescription());
            }
            if (itemDto.getAvailable() != null) {
                itemUpdate.setAvailable(itemDto.getAvailable());
            }
            return mapper.toItemDto(itemRepository.save(mapper.toItem(itemUpdate)));
        } else {
            throw new IncorrectParameterException("itemId или userId");
        }
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text.isBlank() || text == null) {
            List<ItemDto> itemsClear = new ArrayList<>();
            return itemsClear;
        }
        text = text.toLowerCase();
        List<Item> items = itemRepository.searchItems(text);
        return items.stream().map(x -> mapper.toItemDto(x)).collect(Collectors.toList());
    }

    @Override
    public CommentDto createComment(CommentDto commentDto, Long itemId, Long userId) {
        validatorUserId(userRepository.findById(userId), userId);
        validatorComment(commentDto.getText());
        Comment comment = new Comment();
        Booking booking = bookingService.getBookingWithUserBookedItem(itemId, userId);
        if (booking != null) {
            comment.setCreated(LocalDateTime.now());
            comment.setItem(booking.getItem());
            comment.setAuthor(booking.getBooker());
            comment.setText(commentDto.getText());
        } else {
            throw new IncorrectParameterException("userId");
        }
        return mapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public List<CommentDto> getCommentsByItemId(Long itemId) {
        return commentRepository.findAllByItem_Id(itemId,
                        Sort.by(Sort.Direction.DESC, "created")).stream()
                .map(mapper::toCommentDto)
                .collect(toList());
    }
}
