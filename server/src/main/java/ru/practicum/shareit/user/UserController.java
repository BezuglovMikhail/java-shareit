package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
        log.info("Request Get received  to find user`s list in database," +
                " size users in database {}" + usersDto.size());
        return usersDto;
    }

    @PostMapping
    public UserDto saveUser(@RequestBody UserDto userDto) {
        UserDto addedUser = userService.save(userDto);
        log.info("Request Post received to add user: " + addedUser);
        return addedUser;
    }

    @PatchMapping("/{userId}")
    public UserDto update(@RequestBody UserDto userDto, @PathVariable Long userId) {
        UserDto updatedUser = userService.updateUser(userDto, userId);
        log.info("Request Update received to update user, updateUser: " + updatedUser);
        return updatedUser;
    }

    @GetMapping("/{userId}")
    public UserDto findById(@PathVariable long userId) {
        UserDto user = userService.findByIdUser(userId);
        log.info("Request Get received to find user: " + user);
        return user;
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
        log.info("Request Delete received to user delete, userDeleteId = {} ", userId);
    }
}
