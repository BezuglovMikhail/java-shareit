package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;

public interface UserRepository {
   User save(UserDto userDto);

    List<User> findAllUsers();

    User findByIdUser(long userId);

    void deleteUser(long userId);

    User updateUser(UserDto userDto, long userId);

    HashMap<Long, User> getUsers();
    void setIdUser(long idUser);
}
