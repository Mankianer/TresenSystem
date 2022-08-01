package de.mankianer.tresensystem.restcontroller.user;

import de.mankianer.tresensystem.entities.Order;
import de.mankianer.tresensystem.exeptions.ProductNotFoundException;
import de.mankianer.tresensystem.exeptions.UserMisMatchingException;
import de.mankianer.tresensystem.exeptions.UserNotFoundException;
import de.mankianer.tresensystem.exeptions.order.OrderNotFound;
import de.mankianer.tresensystem.restcontroller.dto.OrderDTO;
import de.mankianer.tresensystem.restcontroller.dto.UpdateOrderDTO;
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
  public ResponseEntity<OrderDTO> getOrder(Authentication authentication, @PathVariable Long id) {
    User user = (User) authentication.getPrincipal();
    try {
      return ResponseEntity.ok(new OrderDTO(orderService.getOrderByUserAndId(user, id)));
    } catch (OrderNotFound e) {
      return ResponseEntity.noContent().build();
    }
  }

  @PostMapping("order/")
  public ResponseEntity<OrderDTO> createOrder(Authentication authentication, @RequestBody UpdateOrderDTO orderDTO)
          throws ProductNotFoundException, UserNotFoundException, UserMisMatchingException, OrderNotFound {
    User user = (User) authentication.getPrincipal();
    Order order = orderService.getOrderFromUpdateOrderDTO(orderDTO);
    return ResponseEntity.ok(new OrderDTO(orderService.createOrderbyUser(user, order)));
  }

  @GetMapping("orders/")
  public List<OrderDTO> getAllOrders(Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    return orderService.getOrdersByUsern(user).stream().map(OrderDTO::new).toList();
  }

}
