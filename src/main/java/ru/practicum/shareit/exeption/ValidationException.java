package ru.practicum.shareit.exeption;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidationException extends RuntimeException {
    public ValidationException(final String message) {
        super(message);
        log.error(message);
    }
}
