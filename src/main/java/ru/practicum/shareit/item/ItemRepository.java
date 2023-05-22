package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
   Item save(Item item, long userId);

    List<Item> findAllItemsByIdUser(long userId);

    Item findById(long itemId);

    void deleteItem(long userId, long itemId);

    Item updateItem(Item item, long userId, Long itemId);
}
