package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

public interface UserService {
    UserDto save(UserDto userDto);

    List<UserDto> findAllUsers();

    UserDto findByIdUser(long userId);

    void deleteUser(long userId);

    UserDto updateUser(UserDto userDto, long userId);

    UserRepository getUserRepository();

    User findUserById(Long id);
}
