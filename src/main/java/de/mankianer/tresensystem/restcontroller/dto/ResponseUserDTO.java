package de.mankianer.tresensystem.restcontroller.dto;

import de.mankianer.tresensystem.security.entities.Authority.AuthorityEnum;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ResponseUserDTO {
  private String username;
  private List<String> authorities;
  private boolean isActive;
}
