package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import javax.validation.groups.ConvertGroup;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private final HashMap<Long, List<Long>> usersItemsId = new HashMap<>();

    private HashMap<Long, Item> items = new HashMap<>();
    private long id = 0;

    @Override
    public Item save(Item item, long userId) {
        item.setId(generateId());
        List<Long> newListId = new ArrayList<>();
        if (usersItemsId.size() != 0) {
            newListId = usersItemsId.get(userId);
        }
        newListId.add(item.getId());
        usersItemsId.put(userId, newListId);
        items.put(item.getId(), item);
        return items.get(item.getId());
    }

    @Override
    public List<Item> findAllItemsByIdUser(long userId) {
        List<Item> itemsByIdUser = new ArrayList<>();
        for (Long itemId : usersItemsId.get(userId)) {
            itemsByIdUser.add(items.get(itemId));
        }
        return itemsByIdUser;
    }

    @Override
    public Item findById(long itemId) {
        //Item findItem;
        //for (List<Item> itemsFindById : items.values()) {
          //for (Item item : itemsFindById) {
              //if (item.getId() == itemId) {
                  return items.get(itemId);
              }
          //}
        //}
       // return null;
        //return items.values().stream()
          //      .forEach(n -> n.stream().filter(x -> Objects.equals(x.getid(), itemId)))
            //    ;
   // }

    @Override
    public void deleteItem(long userId, long itemId) {
        items.remove(itemId);
    }

    @Override
    public Item updateItem(Item item, long userId, Long itemId) {
        //List<Long> newListId = usersItemsId.get(userId);
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
        //newListId.set(item.getId(), );
        items.put(item.getId(), itemUpdate);
        return items.get(itemId);
    }

    public long generateId() {
        return ++id;
    }
}
