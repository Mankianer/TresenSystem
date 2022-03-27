package de.mankianer.tresensystem.restcontroller;

import de.mankianer.tresensystem.entities.Order;
import de.mankianer.tresensystem.repository.OrderRepository;
import de.mankianer.tresensystem.security.entities.User;
import de.mankianer.tresensystem.services.OrderService;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderApi {

  private final OrderService orderService;

  public OrderApi(OrderService orderService) {
    this.orderService = orderService;
  }

  @GetMapping("/{id}")
  public Order getOrder(@PathVariable Long id) {
    return orderService.getOrder(id);
  }

  @GetMapping("s/")
  public List<Order> getAllOrders(Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    return orderService.getOrders(user);
  }

  @GetMapping("/")
  public String test() {
    return "test";
  }
}
