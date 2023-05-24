package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<Item> getAllItemByIdUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.findAllItemByIdUser(userId);
    }

    @GetMapping("/{itemId}")
    public Item findById(@PathVariable Long itemId) {
        return itemService.findById(itemId);
    }

    @GetMapping("/search")
    public List<Item> searchFilms(@RequestParam String text) {
       // log.trace("обрабатываем поисковой запрос. Text =" + text);
        return itemService.searchItems(text);
    }

    @PostMapping
    public Item saveUser(@Valid @RequestBody Item item,
                         @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.save(item, userId);
    }

    @PatchMapping("/{itemId}")
    public Item update(@Valid @RequestBody Item item,
                       @RequestHeader("X-Sharer-User-Id") long userId,
                       @PathVariable Long itemId) {
        return itemService.updateItem(item, userId, itemId);
    }

    @DeleteMapping("/{itemId}")
    public void deleteUser(@PathVariable long itemId,
                           @RequestHeader("X-Sharer-User-Id") long userId) {
        itemService.deleteItem(itemId, userId);
    }
}
