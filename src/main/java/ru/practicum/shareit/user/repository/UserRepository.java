package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public interface UserRepository {
    User save(UserDto userDto);

    List<User> findAllUsers();

    User findByIdUser(Long userId);

    void deleteUser(Long userId);

    User updateUser(UserDto userDto, Long userId);

    HashMap<Long, User> getUsers();

    void setIdUser(AtomicLong idUser);
}
