package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;

public interface ItemRepository {
    Item save(ItemDto itemDto, Long userId);

    List<Item> findAllItemsByIdUser(Long userId);

    Item findById(Long itemId);

    void deleteItem(Long userId, Long itemId);

    Item updateItem(ItemDto itemDto, Long userId, Long itemId);

    List<Item> searchItems(String text);

    HashMap<Long, List<Long>> getUsersItemsId();

    HashMap<Long, Item> getItems();

    void setId(long id);
}
