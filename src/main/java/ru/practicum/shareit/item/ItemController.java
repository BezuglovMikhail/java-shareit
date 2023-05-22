package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{userId}")
    public List<Item> getAllItemByIdUser(@PathVariable long userId) {
        return itemService.findAllItemByIdUser(userId);
    }

    @PostMapping
    public Item saveUser(@Valid @RequestBody Item item, @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.save(item, userId);
    }

    @PatchMapping("/{itemId}")
    public Item update(@Valid @RequestBody Item item, @RequestHeader("X-Sharer-User-Id") long userId, @PathVariable Long itemId) {
        return itemService.updateItem(item, userId, itemId);
    }

    @GetMapping("/{itemId}")
    public Item findById(@PathVariable long itemId) {
        return itemService.findById(itemId);
    }

    @DeleteMapping("/{itemId}")
    public void deleteUser(@PathVariable long itemId, @RequestHeader("X-Sharer-User-Id") long userId) {
        itemService.deleteItem(itemId, userId);
    }
}
