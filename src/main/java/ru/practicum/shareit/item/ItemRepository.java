package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Optional<Item> save(Item item, long userId);

    List<Item> findAllItemsByIdUser(long userId);

    Item findByIdItem(long itemId);

    void deleteItem(long userId, long itemId);

    Optional<Item> updateItem(Item item, long userId);
}
