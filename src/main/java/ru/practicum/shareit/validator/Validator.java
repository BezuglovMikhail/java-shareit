package ru.practicum.shareit.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.IncorrectParameterException;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.model.UserEmail;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class Validator {

    public static void validatorRepeatEmail(List<UserEmail> checkEmail, String email) {
        if (!checkEmail.isEmpty()) {
            throw new ValidationException("That user`s email = " +
                    email + " exists in database!");
        }
    }

    public static void validatorEmail(String email) {
        if (email == null || !email.contains("@")) {
            log.info("Error is in box : email = {}", email);
            throw new IncorrectParameterException("email");
        }
    }

    public static void validatorItemIdByUserId(ItemDto itemUpdate, Long userId) {
        if (itemUpdate == null) {
            throw new NotFoundException("User whit id = " + userId + " don`t have items.");
        }
        if (!Objects.equals(itemUpdate.getOwner(), userId)) {
            throw new NotFoundException("User whit id = " + itemUpdate.getId() +
                    " don`t have item whit id = " + userId);
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

    public static void validatorItemAvailable(boolean available) {
        if (!available) {
            throw new IncorrectParameterException("available (that item already booking)");
        }
    }

    public static void validatorStartEndTime(LocalDateTime start, LocalDateTime end) {
        if (start == null ||
                end == null ||
                start.isAfter(end) ||
                start.equals(end) ||
                start.isBefore(LocalDateTime.now())) {
            throw new IncorrectParameterException("start or end");
        }
    }

    public static boolean validatorItemOwner(Long ownerId, Long userId) {
        if (ownerId == null) {
            throw new NotFoundException("User whit id = " + userId + " don`t have items.");
        }
        if (!Objects.equals(ownerId, userId)) {
            throw new NotFoundException("Only the owner of the item can APPROVED the booking!");
        }
        return true;
    }

    public static void validatorComment(String text) {
        if (text == null || text.isBlank()) {
            throw new IncorrectParameterException("text");
        }
    }

    public static void validatorRequestDescription(String text) {
        if (text == null || text.isBlank()) {
            throw new IncorrectParameterException("description");
        }
    }

    public static void validatorRequestSize(Integer size) {
        if (size <= 0) {
            throw new IncorrectParameterException("size");
        }
    }
}
