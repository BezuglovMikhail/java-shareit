package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public Optional<User> save(User user) {
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
    public User updateUser(User user) {
        return repository.updateUser(user);
    }
}
