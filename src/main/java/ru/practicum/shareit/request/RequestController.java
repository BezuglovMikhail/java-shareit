package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
public class RequestController {
    private static final String USER_ID = "X-Sharer-User-Id";
    private final RequestService service;

    @Autowired
    public RequestController(RequestService requestService) {
        this.service = requestService;
    }

    @ResponseBody
    @PostMapping
    public RequestDto create(@RequestBody RequestDto requestDto,
                                 @RequestHeader(USER_ID) Long creatorRequestId) {
        log.info("Request Post received to add item by request whit id  = {}", creatorRequestId);
        return service.create(requestDto, creatorRequestId, LocalDateTime.now());
    }

    @GetMapping("/{requestId}")
    public RequestDto getRequestById(@PathVariable("requestId") Long requestId, @RequestHeader(USER_ID) Long userId) {
        log.info("Request GET received to request whit id = {}", requestId);
        return service.getRequestById(requestId, userId);
    }

    @GetMapping
    public List<RequestDto> getOwnItemRequests(@RequestHeader(USER_ID) Long userId) {
        log.info("Request GET received to request to list requests user`s whit id = {}", userId);
        return service.getOwnRequests(userId);
    }

    @GetMapping("/all")
    public List<RequestDto> getAllItemRequests(@RequestHeader(USER_ID) Long userId,
                                                   @RequestParam(defaultValue = "0") Integer from,
                                                   @RequestParam(required = false) Integer size) {
        log.info("Request GET received to request to list all requests. User`s id = {}", userId);
        return service.getAllRequests(userId, from, size);
    }
}