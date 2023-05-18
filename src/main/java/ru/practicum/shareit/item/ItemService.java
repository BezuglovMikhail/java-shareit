package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    Optional<Item> save(Item item);

    List<Item> findAllItemByIdUser();

    Item findByIdItem(long itemId);

    void deleteItem(long itemId);

    Item updateItem(Item item);
}
