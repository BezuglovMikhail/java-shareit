package ru.practicum.shareit.user;

public class ValidationException extends RuntimeException {
    public ValidationException(final String message) {
        super(message);
    }
}
