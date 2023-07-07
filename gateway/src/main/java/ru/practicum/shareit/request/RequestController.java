package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.user.Constant.USER_ID;

@Controller
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping(path = "/requests")
public class RequestController {
    private final RequestClient requestClient;

    @ResponseBody
    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid RequestDto requestDto,
                                         @RequestHeader(USER_ID) Long creatorRequestId) {
        log.info("Request Post received to add item by request whit id  = {}", creatorRequestId);
        return requestClient.create(requestDto, creatorRequestId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@PathVariable("requestId") Long requestId,
                                                     @RequestHeader(USER_ID) Long userId) {
        log.info("Request GET received to request whit id = {}", requestId);
        return requestClient.getRequestById(userId, requestId);
    }

    @GetMapping
    public ResponseEntity<Object> getOwnItemRequests(@RequestHeader(USER_ID) Long userId) {
        log.info("Request GET received to request to list requests user`s whit id = {}",
                userId);
        return requestClient.getOwnRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(@RequestHeader(USER_ID) Long userId,
                                                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                                     Integer from,
                                                     @RequestParam(required = false) Integer size) {
        log.info("Request GET received to request to list all requests. User`s id = {}",
                userId);
        return requestClient.getAllRequests(userId, from, size);
    }
}
