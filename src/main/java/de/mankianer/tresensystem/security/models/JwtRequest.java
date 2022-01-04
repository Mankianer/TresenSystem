package de.mankianer.tresensystem.security.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class JwtRequest {
  private String username;
  private String password;
}
