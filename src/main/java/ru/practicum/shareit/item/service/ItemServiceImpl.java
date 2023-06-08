package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.IncorrectParameterException;
import ru.practicum.shareit.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.dto.ItemMapper.toItem;
import static ru.practicum.shareit.item.dto.ItemMapper.toItemDto;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {

    @Autowired
    private final ItemRepository itemRepository;

    @Autowired
    private final UserRepository userRepository;

    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ItemDto save(ItemDto itemDto, Long userId) {
        validatorUserId(userId);
        validatorItem(itemDto);
        itemDto.setOwner(userId);
        Item item = itemRepository.save(toItem(itemDto));
        return toItemDto(item);
    }

    @Override
    public List<ItemDto> findAllItemByIdUser(Long userId) {
        validatorUserId(userId);
        List<Item> items = itemRepository.findByOwner(userId);
        return items.stream().map(x -> toItemDto(x)).collect(Collectors.toList());
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
        if (itemId != null & userId != null) {
            ItemDto itemUpdate = findById(itemId);
            validatorItemId(itemUpdate, userId);
            if (itemDto.getName() != null) {
                itemUpdate.setName(itemDto.getName());
            }
            if (itemDto.getDescription() != null) {
                itemUpdate.setDescription(itemDto.getDescription());
            }
            if (itemDto.getAvailable() != null) {
                itemUpdate.setAvailable(itemDto.getAvailable());
            }
            return toItemDto(itemRepository.save(toItem(itemUpdate)));
        } else {
            log.info("Поле itemId или userId - отсутствует");
            throw new IncorrectParameterException("itemId или userId");
        }
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text.isBlank() || text == null) {
            List<ItemDto> itemsClear = new ArrayList<>();
            return itemsClear;
        }
        text = text.toLowerCase();
        List<Item> items = itemRepository.searchItems(text);
        return items.stream().map(x -> toItemDto(x)).collect(Collectors.toList());
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
        if (!userRepository.findById(userId).isPresent()) {
            log.info("Пользователя с id = {} нет", userId);
            throw new NotFoundException("Пользователя с id = " + userId + " нет.");
        }
    }

    public void validatorItemId(ItemDto itemUpdate, Long userId) {
        if (itemUpdate == null) {
            log.info("У пользователя с id = {} нет вещей.", userId);
            throw new NotFoundException("У пользователя с id = " + userId + " нет вещей.");
        }
        if (!Objects.equals(itemUpdate.getOwner(), userId)) {
            log.info("Вещь с id = {} у пользователя с id = {}  не найдена.", itemUpdate.getId(), userId);
            throw new NotFoundException("Вещь с id = " + itemUpdate.getId() +
                    " у пользователя с id = " + userId + " не найдена.");
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
