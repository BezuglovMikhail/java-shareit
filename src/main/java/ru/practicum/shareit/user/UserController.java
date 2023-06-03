package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        List<UserDto> usersDto = userService.findAllUsers();
        log.info("Кол-во пользователей: " + usersDto.size());
        return usersDto;
    }

    @PostMapping
    public UserDto saveUser(@RequestBody UserDto userDto) {
        UserDto addedUser = userService.save(userDto);
        log.info("Пользователь добавлен: " + addedUser);
        return addedUser;
    }

    @PatchMapping("/{userId}")
    public UserDto update(@Valid @RequestBody UserDto userDto, @PathVariable Long userId) {
        UserDto updatedUser = userService.updateUser(userDto, userId);
        log.info("Пользователь обновлен: " + updatedUser);
        return updatedUser;
    }

    @GetMapping("/{userId}")
    public UserDto findById(@PathVariable long userId) {
        UserDto user = userService.findByIdUser(userId);
        log.info("Пользователь получен: " + user);
        return user;
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
        log.info("Пользователь id={} удален", userId);
    }
}
