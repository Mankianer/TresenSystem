package de.mankianer.tresensystem.restcontroller.barkeeper;

import de.mankianer.tresensystem.entities.Order;
import de.mankianer.tresensystem.exeptions.order.MissingValueException;
import de.mankianer.tresensystem.exeptions.order.OrderNotFound;
import de.mankianer.tresensystem.restcontroller.dto.OrderDTO;
import de.mankianer.tresensystem.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
  public ResponseEntity<Order> createOrder(Authentication authentication, @RequestBody OrderDTO orderDTO)
          throws MissingValueException {
    Order order = orderDTO.toOrder();
    return ResponseEntity.ok(orderService.createOrderByBarkeeper(order));
  }

  @GetMapping("")
  public List<OrderDTO> getAllOrders(Authentication authentication) {
    List<OrderDTO> ordersPastTime = orderService.getOrdersPastTime(2).stream().map(OrderDTO::new).collect(Collectors.toList());
    return ordersPastTime;
  }

  @PutMapping("{id}/cancel")
  public ResponseEntity<Order> cancelOrder(Authentication authentication, @PathVariable Long id) throws OrderNotFound {
    Order order = orderService.getOrderById(id);
    order.setCanceledAt(LocalDateTime.now());
    orderService.updateOrder(order);
    return ResponseEntity.ok(order);
  }

}
