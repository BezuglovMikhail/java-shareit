package ru.practicum.shareit.item.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item {
    private Long id;

    //@NotNull
    //@NotBlank
    private String name;

    //@NotNull
    //@NotBlank
    private String description;

    //@NotNull
    private Boolean available;
}
