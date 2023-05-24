package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
   Item save(Item item, Long userId);

    List<Item> findAllItemByIdUser(Long userId);

    Item findById(Long itemId);

    void deleteItem(Long itemId, Long userId);

    Item updateItem(Item item, Long userId, Long itemId);

    List<Item> searchItems(String text);
}
