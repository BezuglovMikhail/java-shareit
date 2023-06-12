package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerId(Long bookerId, Sort sort);

    List<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfter(Long bookerId, LocalDateTime start,
                                                              LocalDateTime end, Sort sort);

    List<Booking> findByBookerIdAndEndIsBefore(Long bookerId, LocalDateTime end, Sort sort);

    List<Booking> findByBookerIdAndStartIsAfter(Long bookerId, LocalDateTime start, Sort sort);

    List<Booking> findByBookerIdAndStatus(Long bookerId, BookingStatus status, Sort sort);

    List<Booking> findByItem_Owner(Long ownerId, Sort sort);

    List<Booking> findByItem_OwnerAndStartIsBeforeAndEndIsAfter(Long ownerId, LocalDateTime start,
                                                                   LocalDateTime end, Sort sort);

    List<Booking> findByItem_OwnerAndEndIsBefore(Long bookerId, LocalDateTime end, Sort sort);

    List<Booking> findByItem_OwnerAndStartIsAfter(Long bookerId, LocalDateTime start, Sort sort);

    List<Booking> findByItem_OwnerAndStatus(Long bookerId, BookingStatus status, Sort sort);

    Booking findFirstByItem_IdAndEndBeforeAndStatusOrderByEndDesc(Long itemId, LocalDateTime end, BookingStatus status);

    Booking findFirstByItem_IdAndStartAfterAndStatusOrderByStartAsc(Long itemId, LocalDateTime end, BookingStatus status);

    Booking findFirstByItem_IdAndBooker_IdAndEndIsBeforeAndStatus(Long itemId, Long userId,
                                                                  LocalDateTime end, BookingStatus status);
}
