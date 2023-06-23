package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
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
    public List<ItemDto> getAllItemByIdUser(@RequestHeader(OWNER) Long userId) {
        List<ItemDto> allItemsByIdUser = itemService.findAllItemByIdUser(userId);
        log.info("Request Get received to list items user`s whit id = {}, size find items = {}.",
                userId, allItemsByIdUser.size());
        return allItemsByIdUser;
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId, @RequestHeader(OWNER) Long ownerId) {
        ItemDto findItem = itemService.getItemById(itemId, ownerId);
        log.info("Request Get received to item " + findItem);
        return findItem;
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        log.info("Request Get received to search text = " + text);
        return itemService.searchItems(text);
    }

    @PostMapping
    public ItemDto saveItem(@Valid @RequestBody ItemDto itemDto,
                            @RequestHeader(OWNER) Long userId) {
        ItemDto addItem = itemService.save(itemDto, userId);
        log.info("Request Post received to add item user`s whit id = {}", userId);
        return addItem;
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@Valid @RequestBody ItemDto itemDto,
                          @RequestHeader(OWNER) long userId,
                          @PathVariable Long itemId) {
        ItemDto updateItem = itemService.updateItem(itemDto, userId, itemId);
        log.info("Request Update received to update item whit id = {} user whit id = {}", updateItem.getId(), userId);
        return updateItem;
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable long itemId,
                           @RequestHeader(OWNER) long userId) {
        itemService.deleteItem(itemId, userId);
        log.info("Request Delete received to delete item whit id = {} user whit id = {}", itemId, userId);
    }

    @ResponseBody
    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestBody CommentDto commentDto, @RequestHeader(OWNER) Long userId,
                                    @PathVariable Long itemId) {
        log.info("Request Post received to add comment by user whit id = {}", userId);
        return itemService.saveComment(commentDto, itemId, userId);
    }
}
