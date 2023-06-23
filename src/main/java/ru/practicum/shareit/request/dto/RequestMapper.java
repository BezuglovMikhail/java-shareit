package ru.practicum.shareit.request.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

@Component
public class RequestMapper {

    private UserMapper userMapper;
    private UserService userService;
    private ItemService itemService;

    @Autowired
    public RequestMapper(UserMapper userMapper, UserService userService, ItemService itemService) {
        this.userMapper = userMapper;
        this.userService = userService;
        this.itemService = itemService;
    }

    public RequestDto toRequestDto(Request request) {
        return new RequestDto(
                request.getId(),
                request.getDescription(),
                userMapper.toUserDto(request.getCreatorRequest()),
                request.getCreated(),
                itemService.getItemsByRequestId(request.getId())
        );
    }

    public Request toRequest(RequestDto requestDto, Long creatorRequestId, LocalDateTime createdTime) {
        return new Request(
                null,
                requestDto.getDescription(),
                userMapper.toUser(userService.findByIdUser(creatorRequestId)),
                createdTime
        );
    }
}
