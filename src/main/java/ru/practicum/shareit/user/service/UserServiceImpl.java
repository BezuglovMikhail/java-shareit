package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
//@Transactional(readOnly = true)
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDto save(UserDto userDto) {
        validatorEmail(userDto.getEmail());
        try {
            return toUserDto(repository.save(toUser(userDto)));
        } catch (DataIntegrityViolationException e) {
            throw new ValidationException("Пользователь с E-mail=" +
                    userDto.getEmail() + " уже существует!");
        }

        /*validatorEmail(userDto.getEmail());
        validatorRepeatEmail(userDto.getEmail());
        User user = repository.save(toUser(userDto));
        return toUserDto(user);*/
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = repository.findAll();
        return mapToUserDto(users);
    }

    @Override
    public UserDto findByIdUser(long userId) {
        //validatorUserId(userId);
        Optional<User> user = repository.findById(userId);
        if (user.isPresent()) {
            return toUserDto(user.get());
        } else {
            log.info("Пользователя с id = {} нет", userId);
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
                validatorRepeatEmail(userDto.getEmail());
            }
            if (userDto.getName() == null) {
                userDto.setName(user.get().getName());
            }
            if (userDto.getEmail() == null) {
                userDto.setEmail(user.get().getEmail());
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

    @Override
    public User findUserById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID=" + id + " не найден!"));
    }


    public void validatorRepeatEmail(String email) {
        if (!repository.findAllByEmail(email).isEmpty()) {
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

    /*public void validatorUserId(long userId) {
        if (repository.findAllById(userId).isEmpty()) {
            log.info("Пользователя с id = {} нет", userId);
            throw new NotFoundException("Пользователя с id = " + userId + " не существует.");
        }
    }*/
}
