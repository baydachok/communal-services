package ru.baydak.dto;

import ru.baydak.model.types.Role;

/**
 * Data Transfer Object (DTO) representing user information.
 * This class encapsulates data related to a user, including the login and role.
 */
public record UserDto
        (
                String login,
                Role role
        ) {
}
