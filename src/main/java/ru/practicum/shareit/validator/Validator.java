package ru.practicum.shareit.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.IncorrectParameterException;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserEmail;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class Validator {

    public static void validatorRepeatEmail(List<UserEmail> checkEmail, String email) {
        if (!checkEmail.isEmpty()) {
            throw new ValidationException("Пользователь с email = " + email + " уже существует.");
        }
    }

    public static void validatorEmail(String email) {
        if (email == null || !email.contains("@")) {
            log.info("Ошибка в поле: email = {}", email);
            throw new IncorrectParameterException("email");
        }
    }

    public static void validatorUserId(Optional<User> user, Long userId) {
        if (!user.isPresent()) {
            throw new NotFoundException("Пользователя с id = " + userId + " нет.");
        }
    }

    public static void validatorItemIdByUserId(ItemDto itemUpdate, Long userId) {
        if (itemUpdate == null) {
            throw new NotFoundException("У пользователя с id = " + userId + " нет вещей.");
        }
        if (!Objects.equals(itemUpdate.getOwner(), userId)) {
            throw new NotFoundException("Вещь с id = " + itemUpdate.getId() +
                    " у пользователя с id = " + userId + " не найдена.");
        }
    }

    public static void validatorItem(ItemDto itemDto) {
        if (itemDto.getAvailable() == null) {
            throw new IncorrectParameterException("available");
        }
        if (itemDto.getName() == null || itemDto.getName().isBlank()) {
            throw new IncorrectParameterException("name");
        }
        if (itemDto.getDescription() == null || itemDto.getDescription().isBlank()) {
            throw new IncorrectParameterException("description");
        }
    }

    public static void validatorItemId(Optional<Item> item, Long itemId) {
        if (!item.isPresent()) {
            throw new NotFoundException("Вещь с id = " + itemId + " не существует.");
        }
    }


    public static void validatorItemAvailable(boolean available) {
        if (!available) {
            throw new IncorrectParameterException("available (вещь забронирована)");
        }
    }

    public static void validatorStartEndTime(LocalDateTime start, LocalDateTime end) {
        if (start == null ||
                end == null ||
                start.isAfter(end) ||
                start.equals(end) ||
                start.isBefore(LocalDateTime.now())) {
            throw new IncorrectParameterException("start или end");
        }
    }

    public static boolean validatorItemOwner(Long ownerId, Long userId) {
        if (ownerId == null) {
            throw new NotFoundException("У пользователя с id = " + userId + " нет вещей.");
        }
        if (!Objects.equals(ownerId, userId)) {
            throw new NotFoundException("Только владелец может подтвердить бронирование");
        }
        return true;
    }

    public static void validatorComment(String text) {
        if (text == null || text.isBlank()) {
            throw new IncorrectParameterException("text");
        }
    }
}
