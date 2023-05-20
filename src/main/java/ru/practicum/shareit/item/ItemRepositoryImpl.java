package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private final HashMap<Long, List<Item>> items = new HashMap<>();
    private long idItem = 0;

    @Override
    public Optional<Item> save(Item item, long userId) {
        item.setIdItem(generateId());
        items.put(userId, List.of(item));
        return items.get(userId).stream()
                .filter(x -> Objects.equals(x.getIdItem(), item.getIdItem()))
                .findFirst();
    }

    @Override
    public List<Item> findAllItemsByIdUser(long userId) {
        return items.get(userId);
    }

    @Override
    public Item findByIdItem(long itemId) {
        //Item findItem;
        for (List<Item> itemsFindById : items.values()) {
          for (Item item : itemsFindById) {
              if (item.getIdItem() == itemId) {
                  return item;
              }
          }
        }
        return null;
        //return items.values().stream()
          //      .forEach(n -> n.stream().filter(x -> Objects.equals(x.getIdItem(), itemId)))
            //    ;
    }

    @Override
    public void deleteItem(long userId, long itemId) {
        items.get(userId).remove(items.get(userId).stream()
                .filter(x -> Objects.equals(x.getIdItem(), itemId))
                .findFirst());
    }

    @Override
    public Optional<Item> updateItem(Item item, long userId) {
        items.put(userId, List.of(item));
        return items.get(userId).stream()
                .filter(x -> Objects.equals(x.getIdItem(), item.getIdItem()))
                .findFirst();
    }

    public long generateId() {
        return ++idItem;
    }
}
