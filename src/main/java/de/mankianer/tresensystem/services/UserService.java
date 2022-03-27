package de.mankianer.tresensystem.services;

import de.mankianer.tresensystem.exeptions.UserMissingException;
import de.mankianer.tresensystem.exeptions.UserExistsException;
import de.mankianer.tresensystem.exeptions.UserNotFoundException;
import de.mankianer.tresensystem.security.UserRepository;
import de.mankianer.tresensystem.security.entities.Authority;
import de.mankianer.tresensystem.security.entities.Authority.AuthorityEnum;
import de.mankianer.tresensystem.security.entities.User;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository,
      PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public List<User> findAll() {
    return userRepository.findAll();
  }

  /**
   * Creates a new user
   * @param new_user new User
   * @return the new User if successful
   * @throws UserExistsException if the user already exists
   */
  public User createUser(User new_user) throws UserExistsException {
    if(userRepository.existsById(new_user.getUsername())) throw new UserExistsException(new_user.getUsername());
    return userRepository.save(new_user);
  }

  /**
   * Creates a new user by an Admin.
   * @param user new User
   * @return the new User if successful
   * @throws UserMissingException if the user didn't exist
   */
  public User updateUser(User user)
      throws UserMissingException {
    if(!userRepository.existsById(user.getUsername())) throw new UserMissingException(user.getUsername());
    return userRepository.save(user);
  }

  /**
   * find a user by its username
   * @param username
   * @return
   * @throws UserNotFoundException
   */
  public User findUser(String username) throws UserNotFoundException {
    return userRepository.findById(username).orElseThrow(() -> new UserNotFoundException(username));
  }

  /**
   * deactivate a user
   * @param username
   * @throws UserNotFoundException
   */
  public User deactivateUser(String username) throws UserNotFoundException {
    if(!userRepository.existsById(username)) throw new UserNotFoundException(username);
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UserNotFoundException(username));
    user.setActive(false);
    return userRepository.save(user);
  }

  /**
   * Creates a new user.
   *
   * @param password get encoded with {@link PasswordEncoder}
   * @return unsaved user
   */
  public User newUser(String username, String password, AuthorityEnum... authorities) {
   return newUser(username, password, false, authorities);
  }

  /**
   * Creates a new user.
   *
   * @param password get encoded with {@link PasswordEncoder} if @param isPasswordEncoded is false
   * @return unsaved user
   */
  public User newUser(String username, String password, boolean isPasswordHashed, AuthorityEnum... authorities) {
    User user = new User();
    user.setUsername(username);
    if(!isPasswordHashed) user.setHashedPassword(passwordEncoder.encode(password));
    List<Authority> authorityList = Arrays.stream(authorities)
        .map(e -> new Authority(null, e, user.getUsername()))
        .collect(Collectors.toList());
    user.setAuthorities(authorityList);
    return user;
  }
}
