package de.mankianer.tresensystem.security;

import de.mankianer.tresensystem.security.entities.User;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends CrudRepository<String, User> {


  Optional<User> findByUsername(String username);
}
