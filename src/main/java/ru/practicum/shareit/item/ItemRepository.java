package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;

public interface ItemRepository {
    Item save(Item item, Long userId);

    List<Item> findAllItemsByIdUser(Long userId);

    Item findById(Long itemId);

    void deleteItem(Long userId, Long itemId);

    Item updateItem(Item item, Long userId, Long itemId);

    public List<Item> searchItems(String text);

    public HashMap<Long, List<Long>> getUsersItemsId();

    public HashMap<Long, Item> getItems();
}
