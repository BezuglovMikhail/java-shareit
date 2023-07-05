package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.RequestDto;

import java.time.LocalDateTime;
import java.util.List;

public interface RequestService {
    RequestDto create(RequestDto requestDto, Long creatorRequestId, LocalDateTime created);

    RequestDto getRequestById(Long requestId, Long userId);

    List<RequestDto> getOwnRequests(Long creatorRequestId);

    List<RequestDto> getAllRequests(Long userId, Integer from, Integer size);
}
