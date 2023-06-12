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
            //log.info("Пользователя с email = {} уже существует", email);
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
            //log.info("Пользователя с id = {} нет", userId);
            throw new NotFoundException("Пользователя с id = " + userId + " нет.");
        }
    }

    public static void validatorItemIdByUserId(ItemDto itemUpdate, Long userId) {
        if (itemUpdate == null) {
            //log.info("У пользователя с id = {} нет вещей.", userId);
            throw new NotFoundException("У пользователя с id = " + userId + " нет вещей.");
        }
        if (!Objects.equals(itemUpdate.getOwner(), userId)) {
            //log.info("Вещь с id = {} у пользователя с id = {}  не найдена.", itemUpdate.getId(), userId);
            throw new NotFoundException("Вещь с id = " + itemUpdate.getId() +
                    " у пользователя с id = " + userId + " не найдена.");
        }
    }

    public static void validatorItem(ItemDto itemDto) {
        if (itemDto.getAvailable() == null) {
            //log.info("Поле available - отсутствует");
            throw new IncorrectParameterException("available");
        }
        if (itemDto.getName() == null || itemDto.getName().isBlank()) {
            //log.info("Поле name - не может отсутствовать или быть пустым");
            throw new IncorrectParameterException("name");
        }
        if (itemDto.getDescription() == null || itemDto.getDescription().isBlank()) {
            //log.info("Поле description - не может отсутствовать или быть пустым");
            throw new IncorrectParameterException("description");
        }
    }

    public static void validatorItemId(Optional<Item> item, Long itemId) {
        if (!item.isPresent()) {
            //log.info("Вещь с id = {} нет", itemId);
            throw new NotFoundException("Вещь с id = " + itemId + " не существует.");
        }
    }


    public static void validatorItemAvailable(boolean available) {
        if (!available) {
            //log.info("Запрошенная вещь с id = {} не доступна для бронирования", itemId);
            throw new IncorrectParameterException("available (вещь забронирована)");
        }
    }

    public static void validatorStartEndTime(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null || start.isAfter(end) || start.equals(end) || start.isBefore(LocalDateTime.now())) {
            //log.info("Ошибка во времени бронирования start = {} или end = {} ", start, end);
            throw new IncorrectParameterException("start или end");
        }
    }

    public static boolean validatorItemOwner(Long ownerId, Long userId) {
        if (ownerId == null) {
            //log.info("У пользователя с id = {} нет вещей.", userId);
            throw new NotFoundException("У пользователя с id = " + userId + " нет вещей.");
        }
        if (!Objects.equals(ownerId, userId)) {
            //log.info("Пользователь с id = {} не владеет запрошенной вещью.", userId);
            throw new NotFoundException("Только владелец может подтвердить бронирование");
        }
        return true;
    }

    public static void validatorComment(String text) {
        if (text == null || text.isBlank()) {
            //log.info("Поле name - не может отсутствовать или быть пустым");
            throw new IncorrectParameterException("text");
        }
    }
}
