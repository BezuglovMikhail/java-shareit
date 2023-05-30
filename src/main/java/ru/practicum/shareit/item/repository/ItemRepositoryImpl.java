package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.dto.ItemMapper.toItem;

@Repository
@Slf4j
public class ItemRepositoryImpl implements ItemRepository {
    private HashMap<Long, Item> items = new HashMap<>();
    private AtomicLong id = new AtomicLong(0);

    @Override
    public Item save(ItemDto itemDto, Long userId) {
        Item item = toItem(itemDto);
        item.setId(id.incrementAndGet());
        item.setOwner(userId);
        items.put(item.getId(), item);
        log.info("Добавлена вещь с id = {} пользователя с id = {}", item.getId(), userId);
        return items.get(item.getId());
    }

    @Override
    public List<Item> findAllItemsByIdUser(Long userId) {
        List<Item> itemsByIdUser = items.values().stream()
                .filter(x -> x.getOwner().equals(userId))
                .collect(Collectors.toList());
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
        itemUpdate.setOwner(userId);
        if (itemDto.getName() != null) {
            itemUpdate.setName(itemDto.getName());
        } else {
            itemUpdate.setName(items.get(itemId).getName());
        }
        if (itemDto.getDescription() != null) {
            itemUpdate.setDescription(itemDto.getDescription());
        } else {
            itemUpdate.setDescription(items.get(itemId).getDescription());
        }
        if (itemDto.getAvailable() != null) {
            itemUpdate.setAvailable(itemDto.getAvailable());
        } else {
            itemUpdate.setAvailable(items.get(itemId).getAvailable());
        }
        items.put(itemId, itemUpdate);
        log.info("Обновлена вещь с id = {}.", itemId);
        return items.get(itemId);
    }

    @Override
    public HashMap<Long, Item> getItems() {
        return items;
    }

    @Override
    public void setId(AtomicLong id) {
        this.id = id;
    }
}
