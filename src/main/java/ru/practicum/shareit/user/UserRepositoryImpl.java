package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final HashMap<Long, User> users = new HashMap<>();
    private long idUser = 0;

    @Override
    public Optional<User> save(User user) {
        user.setId(generateId());
        users.put(user.getId(), user);
        return users.values().stream()
                .filter(x -> Objects.equals(x.getEmail(), user.getEmail()))
                .findFirst();
    }

    @Override
    public List<User> findAllUsers() {
        return users.values().stream().collect(Collectors.toList());
    }

    @Override
    public User findByIdUser(long userId) {
        if (users.containsKey(userId)) {
            return users.get(userId);
        } else {
            return null;
        }
    }

    @Override
    public void deleteUser(long userId) {
        if (users.containsKey(userId)) {
            users.remove(userId);
        } else {
            //throw ;
        }
    }

    @Override
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            return users.put(user.getId(), user);
        } else {
            return null;
        }
    }

    public long generateId() {
        return ++idUser;
    }
}
