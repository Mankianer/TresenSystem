package de.mankianer.tresensystem.security.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
@Entity
public class Authority implements GrantedAuthority {
  @Id
  String authority;
  String user;
}
