package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserEmail;
import ru.practicum.shareit.user.model.UserId;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<UserEmail> findAllByEmailContainingIgnoreCase(String emailSearch);

    List<UserId> findAllByIdContainingIgnoreCase(Long idSearch);
}
