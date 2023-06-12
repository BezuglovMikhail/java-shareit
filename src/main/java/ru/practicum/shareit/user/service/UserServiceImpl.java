package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.validator.Validator;

import java.util.List;
import java.util.Optional;

import static ru.practicum.shareit.user.dto.UserMapper.*;
import static ru.practicum.shareit.validator.Validator.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository, Validator validator) {
        this.repository = repository;
    }

    @Override
    public UserDto save(UserDto userDto) {
        validatorEmail(userDto.getEmail());
        try {
            return toUserDto(repository.save(toUser(userDto)));
        } catch (DataIntegrityViolationException e) {
            throw new ValidationException("Пользователь с email = " +
                    userDto.getEmail() + " уже существует!");
        }
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = repository.findAll();
        return mapToUserDto(users);
    }

    @Override
    public UserDto findByIdUser(long userId) {
        Optional<User> user = repository.findById(userId);
        if (user.isPresent()) {
            return toUserDto(user.get());
        } else {
            throw new NotFoundException("Пользователя с id = " + userId + " не существует.");
        }
    }

    @Override
    public void deleteUser(long userId) {
        findByIdUser(userId);
        repository.deleteById(userId);
    }

    @Override
    public UserDto updateUser(UserDto userDto, long userId) {
        if (userDto.getId() == null) {
            userDto.setId(userId);
        }
        Optional<User> user = repository.findById(userId);
        if (user.isPresent()) {
            if (!user.get().getEmail().equals(userDto.getEmail())) {
                validatorRepeatEmail(repository.findAllByEmail(userDto.getEmail()), userDto.getEmail());
            }
            if (userDto.getName() == null) {
                userDto.setName(user.get().getName());
            }
            if (userDto.getEmail() == null) {
                userDto.setEmail(user.get().getEmail());
            }
            return toUserDto(repository.save(toUser(userDto)));
        } else {
            throw new NotFoundException("Пользователя с id = " + userId + " не существует.");
        }
    }

    @Override
    public User findUserById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID=" + id + " не найден!"));
    }
}
