package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.Pagination;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static ru.practicum.shareit.request.dto.RequestMapper.toRequest;
import static ru.practicum.shareit.request.dto.RequestMapper.toRequestDto;
import static ru.practicum.shareit.user.dto.UserMapper.toUser;
import static ru.practicum.shareit.validator.Validator.validatorRequestDescription;
import static ru.practicum.shareit.validator.Validator.validatorRequestSize;

@Service
public class RequestServiceImpl implements RequestService {
    private final RequestRepository repository;
    private final UserService userService;
    private final ItemService itemService;

    @Autowired
    public RequestServiceImpl(RequestRepository repository,
                              UserService userService,
                              ItemService itemService) {
        this.repository = repository;
        this.userService = userService;
        this.itemService = itemService;
    }

    @Override
    public RequestDto create(RequestDto requestDto, Long creatorRequestId, LocalDateTime created) {
        validatorRequestDescription(requestDto.getDescription());
        UserDto creatorRequest = userService.findByIdUser(creatorRequestId);

        Request request = toRequest(requestDto, toUser(creatorRequest), created);
        return toRequestDto(repository.save(request), creatorRequest,
                itemService.getItemsByRequestId(request.getId()));
    }

    @Override
    public RequestDto getRequestById(Long requestId, Long userId) {
        UserDto creatorRequest = userService.findByIdUser(userId);
        Request request = repository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос с ID=" + requestId + " не найден!"));
        return toRequestDto(request, creatorRequest,
                itemService.getItemsByRequestId(request.getId()));
    }

    @Override
    public List<RequestDto> getOwnRequests(Long creatorRequestId) {
        UserDto creatorRequest = userService.findByIdUser(creatorRequestId);
        return repository.findAllById(creatorRequestId,
                        Sort.by(Sort.Direction.DESC, "created")).stream()
                .map(x -> toRequestDto(x, creatorRequest, itemService.getItemsByRequestId(x.getId())))
                .collect(toList());
    }

    @Override
    public List<RequestDto> getAllRequests(Long creatorRequestId, Integer from, Integer size) {
        UserDto creatorRequest = userService.findByIdUser(creatorRequestId);
        List<RequestDto> listRequestDto = new ArrayList<>();
        Pageable pageable;
        Page<Request> page;
        Pagination pager = new Pagination(from, size);
        Sort sort = Sort.by(Sort.Direction.DESC, "created");

        if (size == null) {
            List<Request> listItemRequest = repository.findAllByIdNotOrderByCreatedDesc(creatorRequestId);
            listRequestDto
                    .addAll(listItemRequest.stream().skip(from)
                            .map(x -> toRequestDto(x, creatorRequest,
                                    itemService.getItemsByRequestId(x.getId())))
                            .collect(toList()));
        } else {
            validatorRequestSize(size);

            for (int i = pager.getIndex(); i < pager.getTotalPages(); i++) {
                pageable =
                        PageRequest.of(i, pager.getPageSize(), sort);
                page = repository.findAllByIdNot(creatorRequestId, pageable);
                listRequestDto.addAll(page.stream().map(x -> toRequestDto(x, creatorRequest,
                                itemService.getItemsByRequestId(x.getId())))
                        .collect(toList()));
                if (!page.hasNext()) {
                    break;
                }
            }
            listRequestDto = listRequestDto.stream().limit(size).collect(toList());
        }
        return listRequestDto;
    }
}
