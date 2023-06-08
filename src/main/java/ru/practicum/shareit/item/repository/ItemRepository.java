package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwner(Long ownerId);

    /*@Query("select it from Item it " +
            "where it.owner like '%' " +
            //"group by it.id "+
            "order by it.id desc")
    List<Item> findAllByOwner(Long userId);*/

    @Query("select it from Item it " +
            "where lower(it.name) like lower(concat('%', :search, '%')) " +
            " or lower(it.description) like lower(concat('%', :search, '%')) " +
            " and it.available = true")
    List<Item> searchItems(@Param("search") String text);

   /* Item save(ItemDto itemDto, Long userId);

    List<Item> findAllItemsByIdUser(Long userId);

    Item findById(Long itemId);

    void deleteItem(Long userId, Long itemId);

    Item updateItem(ItemDto itemDto, Long userId, Long itemId);

    List<Item> searchItems(String text);

    HashMap<Long, Item> getItems();

    void setId(AtomicLong id);*/
}
