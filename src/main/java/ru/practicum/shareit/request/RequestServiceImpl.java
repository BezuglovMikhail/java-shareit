package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.Pagination;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validator.Validator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static ru.practicum.shareit.validator.Validator.validatorRequestDescription;
import static ru.practicum.shareit.validator.Validator.validatorRequestSize;

@Service
public class RequestServiceImpl implements RequestService {
    private final RequestRepository repository;
    private final Validator validator;
    private final RequestMapper mapper;

    private final UserService userService;

    @Autowired
    public RequestServiceImpl(RequestRepository repository,
                              Validator validator, RequestMapper mapper, UserService userService) {
        this.repository = repository;
        this.validator = validator;
        this.mapper = mapper;
        this.userService = userService;
    }

    @Override
    public RequestDto create(RequestDto itemRequestDto, Long creatorRequestId, LocalDateTime created) {
        validatorRequestDescription(itemRequestDto.getDescription());

        Request request = mapper.toRequest(itemRequestDto, creatorRequestId, created);
        return mapper.toRequestDto(repository.save(request));
    }

    @Override
    public RequestDto getRequestById(Long requestId, Long userId) {
        userService.findByIdUser(userId);
        Request request = repository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос с ID=" + requestId + " не найден!"));
        return mapper.toRequestDto(request);
    }

    @Override
    public List<RequestDto> getOwnRequests(Long creatorRequestId) {
        userService.findByIdUser(creatorRequestId);
        return repository.findAllById(creatorRequestId,
                        Sort.by(Sort.Direction.DESC, "created")).stream()
                .map(mapper::toRequestDto)
                .collect(toList());
    }

    @Override
    public List<RequestDto> getAllRequests(Long creatorRequestId, Integer from, Integer size) {
        userService.findByIdUser(creatorRequestId);
        List<RequestDto> listRequestDto = new ArrayList<>();
        Pageable pageable;
        Page<Request> page;
        Pagination pager = new Pagination(from, size);
        Sort sort = Sort.by(Sort.Direction.DESC, "created");

        if (size == null) {

            List<Request> listItemRequest = repository.findAllByIdNotOrderByCreatedDesc(creatorRequestId);
            listRequestDto
                    .addAll(listItemRequest.stream().skip(from).map(mapper::toRequestDto).collect(toList()));
        } else {
            validatorRequestSize(size);
            for (int i = pager.getIndex(); i < pager.getTotalPages(); i++) {
                pageable =
                        PageRequest.of(i, pager.getPageSize(), sort);
                page = repository.findAllByIdNot(creatorRequestId, pageable);
                listRequestDto.addAll(page.stream().map(mapper::toRequestDto).collect(toList()));
                if (!page.hasNext()) {
                    break;
                }
            }
            listRequestDto = listRequestDto.stream().limit(size).collect(toList());
        }
        return listRequestDto;
    }
}
