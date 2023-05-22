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
        validatorEmail(user.getEmail());
        validatorRepeatEmail(user.getEmail());
        return repository.save(user);
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
            if (!repository.getUsers().get(userId).getEmail().equals(user.getEmail())) {
                validatorRepeatEmail(user.getEmail());
            }
            return repository.updateUser(user, userId);
        } else {
            throw new UserNotFoundException("Пользователя с id = " + user.getId() + " не существует.");
        }
    }

    public void validatorRepeatEmail(String email) {
        for (User oldUser : repository.getUsers().values()) {
            if (oldUser.getEmail().equals(email)) {
                throw new ValidationException("Пользователь с email = " + email + " уже существует.");
            }
        }
    }

    public void validatorEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IncorrectParameterException("email");
        }
    }
}
