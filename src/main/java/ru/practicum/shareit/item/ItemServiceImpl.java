package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.IncorrectParameterException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserNotFoundException;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    @Override
    public Item save(Item item, Long userId) {
        validatorUserId(userId);
        validatorItem(item);
        return itemRepository.save(item, userId);
    }

    @Override
    public List<Item> findAllItemByIdUser(Long userId) {
        validatorUserId(userId);
        return itemRepository.findAllItemsByIdUser(userId);
    }

    @Override
    public Item findById(Long itemId) {
        return itemRepository.findById(itemId);
    }

    @Override
    public void deleteItem(Long itemId, Long userId) {
        validatorUserId(userId);
        itemRepository.deleteItem(itemId, userId);
    }

    @Override
    public Item updateItem(Item item, Long userId, Long itemId) {
        validatorUserId(userId);
        validatorItemId(itemId, userId);
        return itemRepository.updateItem(item, userId, itemId);
    }

    @Override
    public List<Item> searchItems(String text) {
        if (text.isBlank() || text == null) {
            List<Item> itemsClear = new ArrayList<>();
            return itemsClear;
        }
        text = text.toLowerCase();
        return itemRepository.searchItems(text);
    }

    public void validatorUserId(Long userId) {
        if (!userRepository.getUsers().containsKey(userId)) {
            throw new UserNotFoundException("Пользователя с id = " + userId + " не найден.");
        }
    }

    public void validatorItemId(Long itemId, Long userId) {
        if (!itemRepository.getUsersItemsId().containsKey(userId)) {
            throw new UserNotFoundException("Пользователь с " + userId + " не найден.");
        }
        if (itemId != null && !itemRepository.getUsersItemsId().get(userId).contains(itemId)) {
            throw new ItemNotFoundException("Вещь с id = " + itemId + " не найдена.");
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
