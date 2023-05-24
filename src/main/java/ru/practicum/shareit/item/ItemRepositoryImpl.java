package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private final HashMap<Long, List<Long>> usersItemsId = new HashMap<>();

    private HashMap<Long, Item> items = new HashMap<>();
    private long id = 0;

    @Override
    public Item save(Item item, Long userId) {
        item.setId(generateId());
        List<Long> newListId = new ArrayList<>();
        if (usersItemsId.containsKey(userId)) {
            newListId.addAll(usersItemsId.get(userId));
        }
        newListId.add(item.getId());
        usersItemsId.put(userId, newListId);
        items.put(item.getId(), item);
        return items.get(item.getId());
    }

    @Override
    public List<Item> findAllItemsByIdUser(Long userId) {
        List<Item> itemsByIdUser = new ArrayList<>();
        for (Long itemId : usersItemsId.get(userId)) {
            itemsByIdUser.add(items.get(itemId));
        }
        return itemsByIdUser;
    }

    @Override
    public Item findById(Long itemId) {
        return items.get(itemId);
    }

    @Override
    public List<Item> searchItems(String text) {
        List<Item> itemsSearch = new ArrayList<>();
        //String textSearch = "%" + text + "%";
        for (Item item : items.values()) {
            if ((item.getName().toLowerCase().contains(text)
                    || item.getDescription().toLowerCase().contains(text))
                    && item.getAvailable()
            ) {
                itemsSearch.add(item);
            }
        }
        //log.info("Список запрашиваемых вещей получен. Длина = {}", items.size());
        return itemsSearch;
    }

    @Override
    public void deleteItem(Long userId, Long itemId) {
        items.remove(itemId);
    }

    @Override
    public Item updateItem(Item item, Long userId, Long itemId) {
        Item itemUpdate = new Item();
        itemUpdate.setId(itemId);
        if (item.getDescription() != null && item.getName() != null && item.getAvailable() != null) {
            itemUpdate.setDescription(item.getDescription());
            itemUpdate.setName(item.getName());
            itemUpdate.setAvailable(item.getAvailable());
        } else if (item.getDescription() == null && item.getName() != null && item.getAvailable() != null) {
            itemUpdate.setDescription(items.get(itemId).getDescription());
            itemUpdate.setName(item.getName());
            itemUpdate.setAvailable(item.getAvailable());
        } else if (item.getDescription() != null && item.getName() == null && item.getAvailable() != null) {
            itemUpdate.setDescription(item.getDescription());
            itemUpdate.setName(items.get(itemId).getName());
            itemUpdate.setAvailable(item.getAvailable());
        } else if (item.getDescription() != null && item.getName() != null && item.getAvailable() == null) {
            itemUpdate.setDescription(item.getDescription());
            itemUpdate.setName(item.getName());
            itemUpdate.setAvailable(items.get(itemId).getAvailable());
        } else if (item.getDescription() == null && item.getName() != null && item.getAvailable() == null) {
            itemUpdate.setDescription(items.get(itemId).getDescription());
            itemUpdate.setName(item.getName());
            itemUpdate.setAvailable(items.get(itemId).getAvailable());
        } else if (item.getDescription() != null && item.getName() == null && item.getAvailable() == null) {
            itemUpdate.setDescription(item.getDescription());
            itemUpdate.setName(items.get(itemId).getName());
            itemUpdate.setAvailable(items.get(itemId).getAvailable());
        } else if (item.getDescription() == null && item.getName() == null && item.getAvailable() != null) {
            itemUpdate.setDescription(items.get(itemId).getDescription());
            itemUpdate.setName(items.get(itemId).getName());
            itemUpdate.setAvailable(item.getAvailable());
        }
        items.put(itemId, itemUpdate);
        return items.get(itemId);
    }

    public long generateId() {
        return ++id;
    }

    public HashMap<Long, List<Long>> getUsersItemsId() {
        return usersItemsId;
    }

    public HashMap<Long, Item> getItems() {
        return items;
    }
}
