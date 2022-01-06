package de.mankianer.tresensystem.security;

import de.mankianer.tresensystem.security.entities.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, String> {


  Optional<User> findByUsername(String username);
}
