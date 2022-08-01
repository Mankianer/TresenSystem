package de.mankianer.tresensystem.security;

import de.mankianer.tresensystem.security.entities.Authority.AuthorityEnum;
import de.mankianer.tresensystem.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

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
      userRepository.save(userService.newUserObject("admin", "admin", AuthorityEnum.ADMIN, AuthorityEnum.USER));
      log.info("Admin was created: admin/admin");
    } catch (Exception e) {
      log.warn("Admin already exists");
    }
  }

}
