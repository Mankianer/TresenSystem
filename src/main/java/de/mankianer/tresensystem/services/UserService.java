package de.mankianer.tresensystem.services;

import de.mankianer.tresensystem.exeptions.UserExistsException;
import de.mankianer.tresensystem.exeptions.UserMissingException;
import de.mankianer.tresensystem.exeptions.UserNotFoundException;
import de.mankianer.tresensystem.restcontroller.dto.ResponseUserDTO;
import de.mankianer.tresensystem.security.UserRepository;
import de.mankianer.tresensystem.security.entities.Authority;
import de.mankianer.tresensystem.security.entities.Authority.AuthorityEnum;
import de.mankianer.tresensystem.security.entities.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
    User oldUser = userRepository.findByUsername(user.getUsername()).orElseThrow(() -> new UserMissingException(user.getUsername()));
    String password = (user.getPassword() == null) ? oldUser.getPassword() : user.getPassword();
    Collection<Authority> authorities = (user.getAuthorities() == null ? oldUser.getAuthorities() : user.getAuthorities());
    newUserObject(oldUser.getUsername(), password, user.getPassword() == null, authorities);
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
  public User newUserObject(String username, String password, AuthorityEnum... authorities) {
    return newUserObject(username, password, false, authorities);
  }

  /**
   * Creates a new user.
   *
   * @param password get encoded with {@link PasswordEncoder} if @param isPasswordEncoded is false
   * @return unsaved user
   */
  public User newUserObject(String username, String password, boolean isPasswordHashed, AuthorityEnum... authorities) {
    List<Authority> authorityList = Arrays.stream(authorities)
            .map(e -> new Authority(null, e, username))
            .collect(Collectors.toList());
    return newUserObject(username, password, isPasswordHashed, authorityList);
  }

  /**
   * Creates a new user.
   *
   * @param password get encoded with {@link PasswordEncoder} if @param isPasswordEncoded is false
   * @return unsaved user
   */
  public User newUserObject(String username, String password, boolean isPasswordHashed, Collection<Authority> authorities) {
    User user = new User();
    user.setUsername(username);
    if (!isPasswordHashed && password != null) {
      user.setHashedPassword(passwordEncoder.encode(password));
    } else {
      user.setHashedPassword(password);
    }
    user.setAuthorities(authorities);
    return user;
  }

  public ResponseUserDTO convertUserToResponseUserDTO(User user) {
    return new ResponseUserDTO(user.getUsername(), user.getAuthorities().stream().map(Authority::getAuthorityEnum)
            .map(Authority.AuthorityEnum::name).toList(), user.isEnabled());
  }
}
