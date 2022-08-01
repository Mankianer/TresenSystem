package de.mankianer.tresensystem.restcontroller.dto;

import de.mankianer.tresensystem.security.entities.User;
import lombok.Data;

@Data
public class UserDTO {

    public UserDTO(User user) {
        this.username = user.getUsername();
    }

    private String username;
}
