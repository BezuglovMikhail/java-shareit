package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    @PostMapping
    public Optional<User> saveUser(@RequestBody User user) {
        return userService.save(user);
    }

    @PatchMapping("/{userId}")
    public User update(@Valid @RequestBody User user, @PathVariable Long userId) {
        return userService.updateUser(user, userId);
    }

    @GetMapping("/{userId}")
    public User findById(@PathVariable long userId) {
        return userService.findByIdUser(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
    }
}
