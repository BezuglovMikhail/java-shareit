package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {
   Item save(Item item, long userId);

    List<Item> findAllItemByIdUser(long userId);

    Item findById(long itemId);

    void deleteItem(long itemId, long userId);

    Item updateItem(Item item, long userId, Long itemId);
}
