package ru.practicum.shareit.item.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

public interface ItemService {
    ItemDto save(ItemDto itemDto, Long userId);

    List<ItemDto> findAllItemByIdUser(Long userId);

    ItemDto findById(Long itemId);

    void deleteItem(Long itemId, Long userId);

    ItemDto updateItem(ItemDto itemDto, Long userId, Long itemId);

    List<ItemDto> searchItems(String text);

    UserRepository getUserRepository();

    ItemRepository getItemRepository();
}
