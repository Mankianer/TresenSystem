package de.mankianer.tresensystem.security.entities;

import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Entity
public class User implements UserDetails {

  @Id
  private String username;

  private String hashedPassword;
  private boolean isActive;

  @OneToMany(mappedBy = "user")
  private Collection<Authority> authorities;

  @Override
  public String getPassword() {
    return hashedPassword;
  }

  @Override
  public boolean isAccountNonExpired() {
    return isActive;
  }

  @Override
  public boolean isAccountNonLocked() {
    return isActive;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return isActive;
  }

  @Override
  public boolean isEnabled() {
    return isActive;
  }
}
