package de.mankianer.tresensystem.security.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
@Entity
public class Authority implements GrantedAuthority {

  @Id
  AuthorityEnum authority;
  String user;

  public enum AuthorityEnum {
    ROLE_USER, ROLE_ADMIN, ROLE_BARKEEPER;
  }

  @Override
  public String getAuthority() {
    return authority.name();
  }
}
