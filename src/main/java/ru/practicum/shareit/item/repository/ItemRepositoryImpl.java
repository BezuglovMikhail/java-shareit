package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

import static ru.practicum.shareit.item.dto.ItemMapper.toItem;

@Repository
@Slf4j
public class ItemRepositoryImpl implements ItemRepository {

    private HashMap<Long, List<Long>> usersItemsId = new HashMap<>();

    private HashMap<Long, Item> items = new HashMap<>();
    private long id = 0;

    @Override
    public Item save(ItemDto itemDto, Long userId) {
        Item item = toItem(itemDto);
        item.setId(generateId());
        List<Long> newListId = new ArrayList<>();
        if (usersItemsId.containsKey(userId)) {
            newListId.addAll(usersItemsId.get(userId));
        }
        newListId.add(item.getId());
        usersItemsId.put(userId, newListId);
        items.put(item.getId(), item);
        log.info("Добавлена вещь с id = {} пользователя с id = {}", item.getId(), userId);
        return items.get(item.getId());
    }

    @Override
    public List<Item> findAllItemsByIdUser(Long userId) {
        List<Item> itemsByIdUser = new ArrayList<>();
        for (Long itemId : usersItemsId.get(userId)) {
            itemsByIdUser.add(items.get(itemId));
        }
        log.info("Найдены вещи пользователя с id = {} - {} шт.", userId, itemsByIdUser.size());
        return itemsByIdUser;
    }

    @Override
    public Item findById(Long itemId) {
        Item findItem = items.get(itemId);
        log.info("Найдена вещь с id = {}.", itemId);
        return findItem;
    }

    @Override
    public List<Item> searchItems(String text) {
        List<Item> itemsSearch = new ArrayList<>();
        for (Item item : items.values()) {
            if ((item.getName().toLowerCase().contains(text)
                    || item.getDescription().toLowerCase().contains(text))
                    && item.getAvailable()
            ) {
                itemsSearch.add(item);
            }
        }
        log.info("Список запрашиваемых вещей получен. Длина = {}", itemsSearch.size());
        return itemsSearch;
    }

    @Override
    public void deleteItem(Long userId, Long itemId) {
        items.remove(itemId);
        log.info("Удалена вещь с id = {}.", itemId);
    }

    @Override
    public Item updateItem(ItemDto itemDto, Long userId, Long itemId) {
        Item itemUpdate = new Item();
        itemUpdate.setId(itemId);
        if (itemDto.getDescription() != null && itemDto.getName() != null && itemDto.getAvailable() != null) {
            itemUpdate.setDescription(itemDto.getDescription());
            itemUpdate.setName(itemDto.getName());
            itemUpdate.setAvailable(itemDto.getAvailable());
        } else if (itemDto.getDescription() == null && itemDto.getName() != null && itemDto.getAvailable() != null) {
            itemUpdate.setDescription(items.get(itemId).getDescription());
            itemUpdate.setName(itemDto.getName());
            itemUpdate.setAvailable(itemDto.getAvailable());
        } else if (itemDto.getDescription() != null && itemDto.getName() == null && itemDto.getAvailable() != null) {
            itemUpdate.setDescription(itemDto.getDescription());
            itemUpdate.setName(items.get(itemId).getName());
            itemUpdate.setAvailable(itemDto.getAvailable());
        } else if (itemDto.getDescription() != null && itemDto.getName() != null && itemDto.getAvailable() == null) {
            itemUpdate.setDescription(itemDto.getDescription());
            itemUpdate.setName(itemDto.getName());
            itemUpdate.setAvailable(items.get(itemId).getAvailable());
        } else if (itemDto.getDescription() == null && itemDto.getName() != null && itemDto.getAvailable() == null) {
            itemUpdate.setDescription(items.get(itemId).getDescription());
            itemUpdate.setName(itemDto.getName());
            itemUpdate.setAvailable(items.get(itemId).getAvailable());
        } else if (itemDto.getDescription() != null && itemDto.getName() == null && itemDto.getAvailable() == null) {
            itemUpdate.setDescription(itemDto.getDescription());
            itemUpdate.setName(items.get(itemId).getName());
            itemUpdate.setAvailable(items.get(itemId).getAvailable());
        } else if (itemDto.getDescription() == null && itemDto.getName() == null && itemDto.getAvailable() != null) {
            itemUpdate.setDescription(items.get(itemId).getDescription());
            itemUpdate.setName(items.get(itemId).getName());
            itemUpdate.setAvailable(itemDto.getAvailable());
        }
        items.put(itemId, itemUpdate);
        log.info("Обновлена вещь с id = {}.", itemId);
        return items.get(itemId);
    }

    public Long generateId() {
        return ++id;
    }

    @Override
    public HashMap<Long, List<Long>> getUsersItemsId() {
        return usersItemsId;
    }

    @Override
    public HashMap<Long, Item> getItems() {
        return items;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }
}
