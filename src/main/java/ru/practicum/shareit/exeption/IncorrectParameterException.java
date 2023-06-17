package ru.practicum.shareit.exeption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IncorrectParameterException extends RuntimeException {

    private final String parameter;

    public IncorrectParameterException(String parameter) {
        this.parameter = parameter;
        log.error("Ошибка в поле: " + getParameter());
    }

    public String getParameter() {
        return parameter;
    }
}
