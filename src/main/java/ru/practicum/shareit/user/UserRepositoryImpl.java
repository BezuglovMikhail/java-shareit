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
        return users.get(userId);
    }

    @Override
    public void deleteUser(long userId) {
        users.remove(userId);
    }

    @Override
    public User updateUser(User user, long userId) {
        User updateUser = new User();
        updateUser.setId(userId);
        if (user.getEmail() != null && user.getName() != null) {
            updateUser.setEmail(user.getEmail());
            updateUser.setName(user.getName());
        } else if (user.getEmail() == null && user.getName() != null) {
            updateUser.setEmail(getUsers().get(userId).getEmail());
            updateUser.setName(user.getName());
        } else if (user.getName() == null && user.getEmail() != null) {
            updateUser.setEmail(user.getEmail());
            updateUser.setName(getUsers().get(userId).getName());
        }
        users.put(userId, updateUser);
        return users.get(userId);
    }

    public long generateId() {
        return ++idUser;
    }

    @Override
    public HashMap<Long, User> getUsers() {
        return users;
    }
}
