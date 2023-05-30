package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.IncorrectParameterException;
import ru.practicum.shareit.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exeption.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.user.dto.UserMapper.toUserDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public UserDto save(UserDto userDto) {
        validatorEmail(userDto.getEmail());
        validatorRepeatEmail(userDto.getEmail());
        return toUserDto(repository.save(userDto));
    }

    @Override
    public List<UserDto> findAllUsers() {
        return repository.findAllUsers()
                .stream()
                .map(x -> toUserDto(x)).collect(Collectors.toList());
    }

    @Override
    public UserDto findByIdUser(long userId) {
        validatorUserId(userId);
        return toUserDto(repository.findByIdUser(userId));
    }

    @Override
    public void deleteUser(long userId) {
        validatorUserId(userId);
        repository.deleteUser(userId);
    }

    @Override
    public UserDto updateUser(UserDto userDto, long userId) {
        if (repository.getUsers().containsKey(userId)) {
            if (!repository.getUsers().get(userId).getEmail().equals(userDto.getEmail())) {
                validatorRepeatEmail(userDto.getEmail());
            }
            return toUserDto(repository.updateUser(userDto, userId));
        } else {
            log.info("Пользователя с id = {} нет", userId);
            throw new NotFoundException("Пользователя с id = " + userId + " не существует.");
        }
    }

    @Override
    public UserRepository getUserRepository() {
        return repository;
    }


    public void validatorRepeatEmail(String email) {
        for (User oldUser : repository.getUsers().values()) {
            if (oldUser.getEmail().equals(email)) {
                log.info("Пользователя с email = {} уже существует", email);
                throw new ValidationException("Пользователь с email = " + email + " уже существует.");
            }
        }
    }

    public void validatorEmail(String email) {
        if (email == null || !email.contains("@")) {
            log.info("Ошибка в поле: email = {}", email);
            throw new IncorrectParameterException("email");
        }
    }

    public void validatorUserId(long userId) {
        if (!repository.getUsers().containsKey(userId)) {
            log.info("Пользователя с id = {} нет", userId);
            throw new NotFoundException("Пользователя с id = " + userId + " не существует.");
        }
    }
}