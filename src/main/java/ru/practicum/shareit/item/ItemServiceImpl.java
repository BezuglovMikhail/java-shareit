package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.IncorrectParameterException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserNotFoundException;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    @Override
    public Item save(Item item, long userId) {
        validatorUserId(userId);
        validatorItem(item);
        return itemRepository.save(item, userId);
    }

    @Override
    public List<Item> findAllItemByIdUser(long userId) {
        validatorUserId(userId);
        return itemRepository.findAllItemsByIdUser(userId);
    }

    @Override
    public Item findById(long itemId) {
        return itemRepository.findById(itemId);
    }

    @Override
    public void deleteItem(long itemId, long userId) {
        validatorUserId(userId);
        itemRepository.deleteItem(itemId, userId);
    }

    @Override
    public Item updateItem(Item item, long userId, Long itemId) {
        validatorUserId(userId);
        //validatorItemId(item.getId());
        return itemRepository.updateItem(item, userId, itemId);
    }

    public void validatorUserId(long userId) {
        if (!userRepository.getUsers().containsKey(userId)) {
            throw new UserNotFoundException("Пользователя с id = " + userId + " не найден.");
        }
    }

    public void validatorItemId(Long itemId) {
        if (itemId == null) {
            throw new ItemNotFoundException("Пользователя с id = " + itemId + " не найден.");
        }
    }

    public void validatorItem(Item item) {
        if (item.getAvailable() == null) {
            throw new IncorrectParameterException("available");
        }
        if (item.getName() == null || item.getName().isBlank()) {
            throw new IncorrectParameterException("name");
        }
        if (item.getDescription() == null || item.getDescription().isBlank()) {
            throw new IncorrectParameterException("description");
        }
    }
}
