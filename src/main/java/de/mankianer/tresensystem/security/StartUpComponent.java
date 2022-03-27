package de.mankianer.tresensystem.security;

import de.mankianer.tresensystem.security.entities.Authority;
import de.mankianer.tresensystem.security.entities.Authority.AuthorityEnum;
import de.mankianer.tresensystem.security.entities.User;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class StartUpComponent {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public StartUpComponent(UserRepository userRepository,
      PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @PostConstruct
  public void init() {
    try {
      userRepository.save(newUser("admin", "admin", AuthorityEnum.ADMIN));
      log.info("Admin was created: admin/admin");
    } catch (Exception e) {
      log.warn("Admin already exists");
    }
  }

  /**
   * Creates a new user.
   *
   * @param password get encoded with {@link PasswordEncoder}
   * @return unsaved user
   */
  public User newUser(String username, String password, AuthorityEnum... authorities) {
    User user = new User();
    user.setUsername(username);
    user.setHashedPassword(passwordEncoder.encode(password));
    List<Authority> authorityList = Arrays.stream(authorities)
        .map(e -> new Authority(null, e, user.getUsername()))
        .collect(Collectors.toList());
    user.setAuthorities(authorityList);
    return user;
  }
}
