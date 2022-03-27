package de.mankianer.tresensystem.services;

import de.mankianer.tresensystem.exeptions.UserMissingException;
import de.mankianer.tresensystem.exeptions.order.UpdateNotAllowedByUser;
import de.mankianer.tresensystem.exeptions.order.UserExistsException;
import de.mankianer.tresensystem.security.UserRepository;
import de.mankianer.tresensystem.security.entities.Authority.AuthorityEnum;
import de.mankianer.tresensystem.security.entities.User;
import java.util.List;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<User> findAll() {
    return userRepository.findAll();
  }

  /**
   * Creates a new user by an Admin.
   * @param admin needs ROLE_ADMIN
   * @param new_user new User
   * @return the new User if successful
   * @throws UpdateNotAllowedByUser if the admin is not allowed to create a new user
   * @throws UserExistsException if the user already exists
   */
  public User createUser(User admin, User new_user) throws UpdateNotAllowedByUser, UserExistsException {
    if (!admin.getAuthorities().contains(AuthorityEnum.ADMIN)) {
      throw new UpdateNotAllowedByUser(admin);
    }
    if(userRepository.existsById(new_user.getUsername())) throw new UserExistsException(new_user.getUsername());
    return userRepository.save(new_user);
  }

  /**
   * Creates a new user by an Admin.
   * @param admin needs ROLE_ADMIN or is the same as user
   * @param user new User
   * @return the new User if successful
   * @throws UpdateNotAllowedByUser if the admin is not allowed to create a new user
   * @throws UserMissingException if the user didn't exist
   */
  public User updateUser(User admin, User user)
      throws UpdateNotAllowedByUser, UserMissingException {
    if (!admin.getAuthorities().contains(AuthorityEnum.ADMIN) || admin.getUsername().equals(user.getUsername())) {
      throw new UpdateNotAllowedByUser(admin);
    }
    if(!userRepository.existsById(user.getUsername())) throw new UserMissingException(user.getUsername());
    return userRepository.save(user);
  }

}
