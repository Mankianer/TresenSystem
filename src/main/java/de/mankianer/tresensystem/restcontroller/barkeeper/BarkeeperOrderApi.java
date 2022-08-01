package de.mankianer.tresensystem.restcontroller.barkeeper;

import de.mankianer.tresensystem.entities.Order;
import de.mankianer.tresensystem.exeptions.order.MissingValueException;
import de.mankianer.tresensystem.exeptions.order.OrderNotFound;
import de.mankianer.tresensystem.restcontroller.dto.OrderDTO;
import de.mankianer.tresensystem.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("bar/order/")
public class BarkeeperOrderApi {

  private final OrderService orderService;

  public BarkeeperOrderApi(OrderService orderService) {
    this.orderService = orderService;
  }

  @GetMapping("{id}")
  public ResponseEntity<Order> getOrder(Authentication authentication, @PathVariable Long id) {
    try {
      return ResponseEntity.ok(orderService.getOrderById(id));
    } catch (OrderNotFound e) {
      return ResponseEntity.noContent().build();
    }
  }

  @PostMapping("{id}")
  public ResponseEntity<Order> createOrder(Authentication authentication, @RequestBody OrderDTO order)
          throws MissingValueException {
    return ResponseEntity.ok(orderService.createOrderByBarkeeper(order.toOrder()));
  }

  @PutMapping("{id}")
  public ResponseEntity<Order> updateOrder(Authentication authentication, @RequestBody OrderDTO order)
          throws OrderNotFound {
    return ResponseEntity.ok(orderService.updateOrder(order.toOrder()));
  }

  @GetMapping("")
  public List<Order> getAllOrders(Authentication authentication) {
    return orderService.getOrdersPastTime(2);
  }

}
