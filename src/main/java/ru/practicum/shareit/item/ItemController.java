package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@Slf4j
public class ItemController {

    private static final String OWNER = "X-Sharer-User-Id";
    @Autowired
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public List<ItemDto> getAllItemByIdUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        List<ItemDto> allItemsByIdUser = itemService.findAllItemByIdUser(userId);
        log.info("Количество вещей пользователя с id = {} - {} шт.", userId, allItemsByIdUser.size());
        return itemService.findAllItemByIdUser(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId, @RequestHeader(OWNER) Long ownerId) {
        ItemDto findItem = itemService.getItemById(itemId, ownerId);
        log.info("Вещь найдена: " + findItem);
        return findItem;
    }

    @GetMapping("/search")
    public List<ItemDto> searchFilms(@RequestParam String text) {
        log.info("Обрабатываем поисковой запрос. Text = " + text);
        return itemService.searchItems(text);
    }

    @PostMapping
    public ItemDto saveUser(@Valid @RequestBody ItemDto itemDto,
                            @RequestHeader("X-Sharer-User-Id") Long userId) {
        ItemDto addItem = itemService.save(itemDto, userId);
        log.info("Добавлена вещь с id = {} пользователя с id = {}", addItem.getId(), userId);
        return addItem;
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@Valid @RequestBody ItemDto itemDto,
                          @RequestHeader("X-Sharer-User-Id") long userId,
                          @PathVariable Long itemId) {
        ItemDto updateItem = itemService.updateItem(itemDto, userId, itemId);
        log.info("Обновлена вещь с id = {} пользователя с id = {}", updateItem.getId(), userId);
        return updateItem;
    }

    @DeleteMapping("/{itemId}")
    public void deleteUser(@PathVariable long itemId,
                           @RequestHeader("X-Sharer-User-Id") long userId) {
        itemService.deleteItem(itemId, userId);
        log.info("Удалена вещь с id = {} пользователя с id = {}", itemId, userId);
    }
}
