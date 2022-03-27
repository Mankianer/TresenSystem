package de.mankianer.tresensystem.restcontroller.dto;

import de.mankianer.tresensystem.security.entities.Authority;
import de.mankianer.tresensystem.security.entities.Authority.AuthorityEnum;
import java.util.Collection;
import java.util.List;
import lombok.Data;

@Data
public class CreateUserDTO {
  private String username;
  private String password;
  private AuthorityEnum[] authorities;
}
