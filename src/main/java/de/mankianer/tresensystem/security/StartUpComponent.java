package de.mankianer.tresensystem.security;

import de.mankianer.tresensystem.security.entities.Authority;
import de.mankianer.tresensystem.security.entities.Authority.AuthorityEnum;
import de.mankianer.tresensystem.security.entities.User;
import de.mankianer.tresensystem.services.UserService;
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
  private final UserService userService;

  public StartUpComponent(UserRepository userRepository,
      PasswordEncoder passwordEncoder, UserService userService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.userService = userService;
  }

  @PostConstruct
  public void init() {
    try {
      userRepository.save(userService.newUser("admin", "admin", AuthorityEnum.ADMIN, AuthorityEnum.USER));
      log.info("Admin was created: admin/admin");
    } catch (Exception e) {
      log.warn("Admin already exists");
    }
  }

}
