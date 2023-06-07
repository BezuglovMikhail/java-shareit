package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.IncorrectParameterException;
import ru.practicum.shareit.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemIdAndOwner;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.dto.ItemMapper.toItem;
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
        Item item = itemRepository.save(toItem(itemDto));
        return toItemDto(item);
    }

    @Override
    public List<ItemDto> findAllItemByIdUser(Long userId) {
        validatorUserId(userId);
        return itemRepository.findAllItemsByIdUser(userId);
    }

    @Override
    public ItemDto findById(Long itemId) {
        if (itemId != null) {
            Optional<Item> item = itemRepository.findById(itemId);
            if (item.isPresent()) {
                return toItemDto(item.get());
            } else {
                log.info("Вещь с id = {} не найдена", itemId);
                throw new NotFoundException("Вещь с id = " + itemId + " не найдена.");
            }
        } else {
            log.info("Поле itemId - отсутствует");
            throw new IncorrectParameterException("itemId или userId");
        }
    }

    @Override
    public void deleteItem(Long itemId, Long userId) {
        validatorUserId(userId);
        itemRepository.deleteById(itemId);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long userId, Long itemId) {
        validatorItemId(itemId, userId);
        return toItemDto(itemRepository.save(toItem(itemDto)));
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text.isBlank() || text == null) {
            List<ItemDto> itemsClear = new ArrayList<>();
            return itemsClear;
        }
        text = text.toLowerCase();
        return itemRepository.searchItems(text);
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
        if (!userRepository.findById(userId).isEmpty()) {
            log.info("Пользователя с id = {} нет", userId);
            throw new NotFoundException("Пользователя с id = " + userId + " нет.");
        }
    }

    public void validatorItemId(Long itemId, Long userId) {
        if (itemId != null & userId != null) {
            List<ItemIdAndOwner> itemIdAndOwners = itemRepository.findAllByIdContainingIgnoreCase(itemId);
            if (itemIdAndOwners.stream()
                    .filter(x -> x.getOwner() == userId)
                    .collect(Collectors.toList())
                    .isEmpty()) {
                log.info("У пользователя с id = {} нет вещей.", userId);
                throw new NotFoundException("У пользователя с id = " + userId + " нет вещей.");
            }
            if (itemIdAndOwners.stream()
                    .filter(x -> x.getOwner() == userId & x.getId() == itemId)
                    .collect(Collectors.toList())
                    .isEmpty()) {
                log.info("Вещь с id = {} у пользователя с id = {}  не найдена.", itemId, userId);
                throw new NotFoundException("Вещь с id = " + itemId +
                        " у пользователя с id = " + userId + " не найдена.");
            }
        } else {
            log.info("Поле itemId или userId - отсутствует");
            throw new IncorrectParameterException("itemId или userId");
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
