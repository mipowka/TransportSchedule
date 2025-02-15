package org.example.transportschedule.model.dto;

import org.example.transportschedule.model.enums.Role;

public record UserDTO(
        String username,
        String password,
        Role role
) {
}
