package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemIdAndOwner;
import ru.practicum.shareit.item.model.ItemOwner;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserId;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    //List<ItemOwner> findAllByOwnerContainingIgnoreCase(Long ownerSearch);

    List<ItemIdAndOwner> findAllByIdContainingIgnoreCase(Long idSearch);

    @Query("select new ru.practicum.shareit.item.dto(it.user.id, it.name, it.description, it.available))" +
            "from Item as it " +
            "where it.owner like ?1 " +
            //"group by it.id "+
            "order by it.id desc")
    List<ItemDto> findAllItemsByIdUser(Long userId);

    @Query("select new ru.practicum.shareit.item.dto(it.user.id, it.name, it.description, it.available))" +
            "from Item as it " +
            "where lower(it.name) like ?1 or lower(it.description) like ?1 " +
            //"group by it.id "+
            "order by it.id desc")
    List<ItemDto> searchItems(String text);

   /* Item save(ItemDto itemDto, Long userId);

    List<Item> findAllItemsByIdUser(Long userId);

    Item findById(Long itemId);

    void deleteItem(Long userId, Long itemId);

    Item updateItem(ItemDto itemDto, Long userId, Long itemId);

    List<Item> searchItems(String text);

    HashMap<Long, Item> getItems();

    void setId(AtomicLong id);*/
}
