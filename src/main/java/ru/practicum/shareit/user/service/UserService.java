package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDto save(UserDto userDto);

    List<UserDto> findAllUsers();

    UserDto findByIdUser(long userId);

    void deleteUser(long userId);

    UserDto updateUser(UserDto userDto, long userId);

    Optional<User> findUserById(Long id);
}
