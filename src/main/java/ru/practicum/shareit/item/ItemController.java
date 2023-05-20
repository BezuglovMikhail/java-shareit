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
    public List<Item> getAllItemByIdUser(long userId) {
        return itemService.findAllItemByIdUser(userId);
    }

    @PostMapping
    public Optional<Item> saveUser(@RequestBody Item item, long userId) {
        return itemService.save(item, userId);
    }

    @PutMapping
    public Optional<Item> update(@Valid @RequestBody Item item, long userId) {
        return itemService.updateItem(item, userId);
    }

    @GetMapping("/{itemId}")
    public Item findById(@PathVariable long itemId) {
        return itemService.findByIdItem(itemId);
    }

    @DeleteMapping("/{itemId}")
    public void deleteUser(@PathVariable long itemId, long userId) {
        itemService.deleteItem(itemId, userId);
    }
}
