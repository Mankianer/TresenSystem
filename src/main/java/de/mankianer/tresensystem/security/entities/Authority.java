package de.mankianer.tresensystem.security.entities;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Authority implements GrantedAuthority {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;


  @Enumerated(EnumType.STRING)
  AuthorityEnum authorityEnum;
  String user;

  public enum AuthorityEnum {
    USER, ADMIN, BARKEEPER, TREASURER;
  }

  public String getAuthority() {
    return authorityEnum.name();
  }
}
