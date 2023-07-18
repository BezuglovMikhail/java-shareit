package ru.practicum.shareit.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface RequestRepository extends PagingAndSortingRepository<Request, Long> {
    List<Request> findAllById(Long creatorRequestId, Sort sort);

    Page<Request> findAllByIdNot(Long userId, Pageable pageable);

    List<Request> findAllByIdNotOrderByCreatedDesc(Long userId);
}
