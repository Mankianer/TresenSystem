package de.mankianer.tresensystem.restcontroller.user;

import de.mankianer.tresensystem.entities.Order;
import de.mankianer.tresensystem.exeptions.ProductNotFoundException;
import de.mankianer.tresensystem.exeptions.order.OrderNotFound;
import de.mankianer.tresensystem.restcontroller.dto.OrderDTO;
import de.mankianer.tresensystem.security.entities.User;
import de.mankianer.tresensystem.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user/")
public class OrderApi {

  private final OrderService orderService;

  public OrderApi(OrderService orderService) {
    this.orderService = orderService;
  }

  @GetMapping("order/{id}")
  public ResponseEntity<Order> getOrder(Authentication authentication, @PathVariable Long id) {
    User user = (User) authentication.getPrincipal();
    try {
      return ResponseEntity.ok(orderService.getOrderByUserAndId(user, id));
    } catch (OrderNotFound e) {
      return ResponseEntity.noContent().build();
    }
  }

  @PostMapping("order/")
  public ResponseEntity<Order> createOrder(Authentication authentication, @RequestBody OrderDTO orderDTO)
          throws ProductNotFoundException {
    User user = (User) authentication.getPrincipal();
    return ResponseEntity.ok(orderService.createOrderbyUser(user, orderDTO.toOrder()));
  }

  @GetMapping("orders/")
  public List<Order> getAllOrders(Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    return orderService.getOrdersByUsern(user);
  }

}
