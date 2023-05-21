package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.IncorrectParameterException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public Optional<User> save(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new IncorrectParameterException("email");
        }

        if (validator(user.getEmail())) {
            return repository.save(user);
        } else {
            throw new ValidationException("Пользователь с email = " + user.getEmail() + " уже существует.");
        }
    }

    @Override
    public List<User> findAllUsers() {
        return repository.findAllUsers();
    }

    @Override
    public User findByIdUser(long userId) {
        return repository.findByIdUser(userId);
    }

    @Override
    public void deleteUser(long userId) {
        repository.deleteUser(userId);
    }

    @Override
    public User updateUser(User user, long userId) {
        if (repository.getUsers().containsKey(userId)) {
            return repository.updateUser(user);
        } else {
            throw new UserNotFoundException("Пользователя с id = " + user.getId() + " не существует.");
        }

    }

    public boolean validator(String email) {
        for (User oldUser : repository.getUsers().values()) {
            if (oldUser.getEmail().equals(email)) {
                return false;
            }
        }
        return true;
    }
}
