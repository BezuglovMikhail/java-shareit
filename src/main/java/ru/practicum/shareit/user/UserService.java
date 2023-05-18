package ru.practicum.shareit.user;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> save(User user);

    List<User> findAllUsers();

    User findByIdUser(long userId);

    void deleteUser(long userId);

    User updateUser(User user);
}
