package ru.practicum.shareit.item.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemMapper {

    private BookingService bookingService;

    @Autowired
    @Lazy
    public ItemMapper(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner()
        );
    }

    public static Item toItem(ItemDto itemDto) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                itemDto.getOwner()
        );
    }

    public ItemDto toItemExtDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                //item.getRequestId() != null ? item.getRequestId() : null,
                bookingService.getLastBooking(item.getId()),
                bookingService.getNextBooking(item.getId())
        );
    }
}
