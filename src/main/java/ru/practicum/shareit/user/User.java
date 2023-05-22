package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private long id;
    //@Email
    //@NotNull
    //@NotBlank
    private String email;

    private String name;
}
