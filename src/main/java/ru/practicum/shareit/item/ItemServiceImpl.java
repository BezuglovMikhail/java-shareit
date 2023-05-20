package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository repository;

    @Override
    public Optional<Item> save(Item item, long userId) {
        return repository.save(item, userId);
    }

    @Override
    public List<Item> findAllItemByIdUser(long userId) {
        return repository.findAllItemsByIdUser(userId);
    }

    @Override
    public Item findByIdItem(long itemId) {
        return repository.findByIdItem(itemId);
    }

    @Override
    public void deleteItem(long itemId, long userId) {
        repository.deleteItem(itemId, userId);
    }

    @Override
    public Optional<Item> updateItem(Item item, long userId) {
        return repository.updateItem(item, userId);
    }
}
