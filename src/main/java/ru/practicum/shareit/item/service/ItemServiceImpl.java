package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.IncorrectParameterException;
import ru.practicum.shareit.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.dto.ItemMapper.toItemDto;

@Service
@RequiredArgsConstructor
@Slf4j
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
        if (itemRepository.getItems().containsKey(itemId)) {
            return toItemDto(itemRepository.findById(itemId));
        } else {
            log.info("Вещь с id = {} не найдена", itemId);
            throw new NotFoundException("Вещь с id = " + itemId + " не найдена.");
        }
    }

    @Override
    public void deleteItem(Long itemId, Long userId) {
        validatorUserId(userId);
        itemRepository.deleteItem(itemId, userId);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long userId, Long itemId) {
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

    @Override
    public UserRepository getUserRepository() {
        return userRepository;
    }

    @Override
    public ItemRepository getItemRepository() {
        return itemRepository;
    }

    public void validatorUserId(Long userId) {
        if (!userRepository.getUsers().containsKey(userId)) {
            log.info("Пользователя с id = {} нет", userId);
            throw new NotFoundException("Пользователя с id = " + userId + " нет.");
        }
    }

    public void validatorItemId(Long itemId, Long userId) {
        if (itemRepository.getItems().values().stream()
                .filter(x -> x.getOwner().equals(userId))
                .collect(Collectors.toList()).isEmpty()) {
            log.info("У пользователя с id = {} нет вещей.", userId);
            throw new NotFoundException("У пользователя с id = " + userId + " нет вещей.");
        }
        if (itemId != null && !itemRepository.getItems().get(itemId).getOwner().equals(userId)) {
            log.info("Вещь с id = {} у пользователя с id = {}  не найдена.", itemId, userId);
            throw new NotFoundException("Вещь с id = " + itemId + " у пользователя с id = " + userId + " не найдена.");
        }
    }

    public void validatorItem(ItemDto itemDto) {
        if (itemDto.getAvailable() == null) {
            log.info("Поле available - отсутствует");
            throw new IncorrectParameterException("available");
        }
        if (itemDto.getName() == null || itemDto.getName().isBlank()) {
            log.info("Поле name - не может отсутствовать или быть пустым");
            throw new IncorrectParameterException("name");
        }
        if (itemDto.getDescription() == null || itemDto.getDescription().isBlank()) {
            log.info("Поле description - не может отсутствовать или быть пустым");
            throw new IncorrectParameterException("description");
        }
    }
}
