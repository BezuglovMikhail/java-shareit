package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static ru.practicum.shareit.user.Constant.USER_ID;

@RestController
@RequestMapping("/items")
@Slf4j
public class ItemController {

    @Autowired
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public List<ItemDto> getAllItemByIdUser(@RequestHeader(USER_ID) Long userId) {
        List<ItemDto> allItemsByIdUser = itemService.findAllItemByIdUser(userId);
        log.info("Request Get received to list items user`s whit id = {}, size find items = {}.",
                userId, allItemsByIdUser.size());
        return allItemsByIdUser;
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId, @RequestHeader(USER_ID) Long ownerId) {
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
    public ItemDto saveItem(@RequestBody ItemDto itemDto,
                            @RequestHeader(USER_ID) Long userId) {
        ItemDto addItem = itemService.save(itemDto, userId);
        log.info("Request Post received to add item user`s whit id = {}", userId);
        return addItem;
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody ItemDto itemDto,
                          @RequestHeader(USER_ID) long userId,
                          @PathVariable Long itemId) {
        ItemDto updateItem = itemService.updateItem(itemDto, userId, itemId);
        log.info("Request Update received to update item whit id = {} user whit id = {}", updateItem.getId(), userId);
        return updateItem;
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable long itemId,
                           @RequestHeader(USER_ID) long userId) {
        itemService.deleteItem(itemId, userId);
        log.info("Request Delete received to delete item whit id = {} user whit id = {}", itemId, userId);
    }

    @ResponseBody
    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestBody CommentDto commentDto, @RequestHeader(USER_ID) Long userId,
                                    @PathVariable Long itemId) {
        log.info("Request Post received to add comment by user whit id = {}", userId);
        return itemService.saveComment(commentDto, itemId, userId);
    }
}
