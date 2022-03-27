package de.mankianer.tresensystem.security.entities;

import de.mankianer.tresensystem.entities.Order;
import de.mankianer.tresensystem.security.entities.Authority.AuthorityEnum;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class User implements UserDetails {

  @Id
  private String username;

  private String hashedPassword;

  private boolean isActive = true;


  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
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
