package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    ItemDto save(ItemDto itemDto, Long userId);

    List<ItemDto> findAllItemByIdUser(Long userId);

    ItemDto findById(Long itemId);

    Optional<Item> findItemById(Long id);

    ItemDto getItemById(Long id, Long userId);

    void deleteItem(Long itemId, Long userId);

    ItemDto updateItem(ItemDto itemDto, Long userId, Long itemId);

    List<ItemDto> searchItems(String text);

    CommentDto createComment(CommentDto commentDto, Long itemId, Long userId);

    List<CommentDto> getCommentsByItemId(Long itemId);
}
