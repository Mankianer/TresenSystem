package de.mankianer.tresensystem.security;

import de.mankianer.tresensystem.security.entities.Authority;
import de.mankianer.tresensystem.security.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {


  Optional<User> findByUsername(String username);

  List<User> findAllByIsActiveAndAuthorities_AuthorityEnum(boolean isActive, Authority.AuthorityEnum authorityEnum);
}
