package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.IncorrectParameterException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.exeption.ItemNotFoundException;
import ru.practicum.shareit.user.exeption.UserNotFoundException;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.dto.ItemMapper.toItemDto;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    @Override
    public ItemDto save(ItemDto itemDto, Long userId) {
        validatorUserId(userId);
        validatorItem(itemDto);
        return toItemDto(itemRepository.save(itemDto, userId));
    }

    @Override
    public List<ItemDto> findAllItemByIdUser(Long userId) {
        validatorUserId(userId);
        return itemRepository.findAllItemsByIdUser(userId).stream()
                .map(x -> toItemDto(x)).collect(Collectors.toList());
    }

    @Override
    public ItemDto findById(Long itemId) {
        return toItemDto(itemRepository.findById(itemId));
    }

    @Override
    public void deleteItem(Long itemId, Long userId) {
        validatorUserId(userId);
        itemRepository.deleteItem(itemId, userId);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long userId, Long itemId) {
        validatorUserId(userId);
        validatorItemId(itemId, userId);
        return toItemDto(itemRepository.updateItem(itemDto, userId, itemId));
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text.isBlank() || text == null) {
            List<ItemDto> itemsClear = new ArrayList<>();
            return itemsClear;
        }
        text = text.toLowerCase();
        return itemRepository.searchItems(text).stream()
                .map(x -> toItemDto(x)).collect(Collectors.toList());
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

    public void validatorItem(ItemDto itemDto) {
        if (itemDto.getAvailable() == null) {
            throw new IncorrectParameterException("available");
        }
        if (itemDto.getName() == null || itemDto.getName().isBlank()) {
            throw new IncorrectParameterException("name");
        }
        if (itemDto.getDescription() == null || itemDto.getDescription().isBlank()) {
            throw new IncorrectParameterException("description");
        }
    }
}
