package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.IncorrectParameterException;
import ru.practicum.shareit.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exeption.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static ru.practicum.shareit.user.dto.UserMapper.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public UserDto save(UserDto userDto) {
        validatorEmail(userDto.getEmail());
        validatorRepeatEmail(userDto.getEmail());
        User user = repository.save(toUser(userDto));
        return toUserDto(user);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = repository.findAll();
        return mapToUserDto(users);
    }

    @Override
    public UserDto findByIdUser(long userId) {
        validatorUserId(userId);
        return toUserDto(repository.findById(userId).get());
    }

    @Override
    public void deleteUser(long userId) {
        validatorUserId(userId);
        repository.deleteById(userId);
    }

    @Override
    public UserDto updateUser(UserDto userDto, long userId) {
        Optional<User> user = repository.findById(userId);
        if (user.isEmpty()) {
            if (!user.get().getEmail().equals(userDto.getEmail())) {
                validatorRepeatEmail(userDto.getEmail());
            }
            return toUserDto(repository.save(toUser(userDto)));
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
        if (repository.findAllByEmailContainingIgnoreCase(email).isEmpty()) {
            log.info("Пользователя с email = {} уже существует", email);
            throw new ValidationException("Пользователь с email = " + email + " уже существует.");
        }
    }

    public void validatorEmail(String email) {
        if (email == null || !email.contains("@")) {
            log.info("Ошибка в поле: email = {}", email);
            throw new IncorrectParameterException("email");
        }
    }

    public void validatorUserId(long userId) {
        if (repository.findAllByIdContainingIgnoreCase(userId).isEmpty()) {
            log.info("Пользователя с id = {} нет", userId);
            throw new NotFoundException("Пользователя с id = " + userId + " не существует.");
        }
    }
}
