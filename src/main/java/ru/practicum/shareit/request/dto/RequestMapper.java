package ru.practicum.shareit.request.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class RequestMapper {

    public static RequestDto toRequestDto(Request request, UserDto userDto, List<ItemDto> itemDtoList) {
        return new RequestDto(
                request.getId(),
                request.getDescription(),
                userDto,
                request.getCreated(),
                itemDtoList
        );
    }

    public static Request toRequest(RequestDto requestDto, User user, LocalDateTime createdTime) {
        return new Request(
                null,
                requestDto.getDescription(),
                user,
                createdTime
        );
    }
}
