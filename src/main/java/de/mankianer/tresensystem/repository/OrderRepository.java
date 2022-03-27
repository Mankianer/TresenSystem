package de.mankianer.tresensystem.repository;

import de.mankianer.tresensystem.entities.Order;
import de.mankianer.tresensystem.security.entities.User;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  public List<Order> findAllByPurchaser(User user);

  public List<Order> findAllByCreatedAtAfter(LocalDateTime date);
}
