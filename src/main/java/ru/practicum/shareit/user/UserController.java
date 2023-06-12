package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@Slf4j
public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

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
    public UserDto update(@RequestBody UserDto userDto, @PathVariable Long userId) {
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
