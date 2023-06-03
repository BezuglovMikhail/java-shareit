package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static ru.practicum.shareit.user.dto.UserMapper.toUser;

@Repository
@Slf4j
public class UserRepositoryImpl implements UserRepository {

    private final HashMap<Long, User> users = new HashMap<>();
    private AtomicLong idUser = new AtomicLong(0);

    @Override
    public User save(UserDto userDto) {
        User newUser = toUser(userDto);
        newUser.setId(idUser.incrementAndGet());
        users.put(newUser.getId(), newUser);
        log.info("Пользователь с id = {} добавлен", newUser.getId());
        return users.get(newUser.getId());
    }

    @Override
    public List<User> findAllUsers() {
        List<User> allUsers = users.values().stream().collect(Collectors.toList());
        log.info("Список пользователей получен. Длина = {}", allUsers.size());
        return allUsers;
    }

    @Override
    public User findByIdUser(Long userId) {
        log.info("Найден пользователь c id = {} ", userId);
        return users.get(userId);
    }

    @Override
    public void deleteUser(Long userId) {
        users.remove(userId);
        log.info("Пользователь c id = {} удален", userId);
    }

    @Override
    public User updateUser(UserDto userDto, Long userId) {
        User updateUser = new User();
        updateUser.setId(userId);
        if (userDto.getName() != null) {
            updateUser.setName(userDto.getName());
        } else {
            updateUser.setName(getUsers().get(userId).getName());
        }
        if (userDto.getEmail() != null) {
            updateUser.setEmail(userDto.getEmail());
        } else {
            updateUser.setEmail(getUsers().get(userId).getEmail());
        }
        users.put(userId, updateUser);
        log.info("Пользователь c id = {} обновлен", userId);
        return users.get(userId);
    }

    @Override
    public HashMap<Long, User> getUsers() {
        return users;
    }

    @Override
    public void setIdUser(AtomicLong idUser) {
        this.idUser = idUser;
    }
}
