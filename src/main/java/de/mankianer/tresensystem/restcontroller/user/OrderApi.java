package de.mankianer.tresensystem.restcontroller.user;

import de.mankianer.tresensystem.entities.Order;
import de.mankianer.tresensystem.exeptions.order.OrderNotFound;
import de.mankianer.tresensystem.exeptions.order.UpdateNotAllowedByUser;
import de.mankianer.tresensystem.security.entities.User;
import de.mankianer.tresensystem.services.OrderService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user/")
public class OrderApi {

  private final OrderService orderService;

  public OrderApi(OrderService orderService) {
    this.orderService = orderService;
  }

  @GetMapping("order/{id}")
  public ResponseEntity getOrder(Authentication authentication, @PathVariable Long id) {
    User user = (User) authentication.getPrincipal();
    try {
      return ResponseEntity.ok(orderService.getOrderByUserAndId(user, id));
    } catch (OrderNotFound e) {
      return ResponseEntity.noContent().build();
    }
  }

  @PostMapping("order/")
  public ResponseEntity createOrder(Authentication authentication, @RequestBody Order order) {
    User user = (User) authentication.getPrincipal();
    return ResponseEntity.ok(orderService.createOrderbyUser(user, order));
  }

  @PutMapping("order/{id}")
  public ResponseEntity updateOrder(Authentication authentication, @RequestBody Order order)
      throws OrderNotFound {
    return ResponseEntity.ok(orderService.updateOrder(order));
  }

  @GetMapping("orders/")
  public List<Order> getAllOrders(Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    return orderService.getOrdersByUsern(user);
  }

}
