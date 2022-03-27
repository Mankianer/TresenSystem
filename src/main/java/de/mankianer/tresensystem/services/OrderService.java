package de.mankianer.tresensystem.services;

import de.mankianer.tresensystem.entities.Order;
import de.mankianer.tresensystem.exeptions.order.OrderNotFound;
import de.mankianer.tresensystem.exeptions.order.UpdateNotAllowedByUser;
import de.mankianer.tresensystem.repository.OrderRepository;
import de.mankianer.tresensystem.security.entities.Authority.AuthorityEnum;
import de.mankianer.tresensystem.security.entities.User;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  private final OrderRepository orderRepository;

  public OrderService(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  /**
   * @param user
   * @return the list of orders of the user
   */
  public List<Order> getOrders(User user) {
    if(user.getAuthorities().contains(AuthorityEnum.BARKEEPER)) {
      return orderRepository.findAllByCreatedAtAfter(LocalDateTime.now().minusHours(2));
    }
    return orderRepository.findAllByPurchaser(user);
  }

  /**
   * @param id
   * @return the new order
   */
  public Order createOrder(User user, Order order) {
    order.setPurchaser(user);
    return orderRepository.save(order);
  }

  public Order getOrder(Long id) {
    return orderRepository.findById(id).orElse(null);
  }

  public Order updateOrder(User user, Order order) throws OrderNotFound, UpdateNotAllowedByUser {
    Order orderToUpdate = orderRepository.findById(order.getId())
        .orElseThrow(() -> new OrderNotFound("id: " + order.getId()));
    if (orderToUpdate.getPurchaser().equals(user)) {
      return orderRepository.save(order);
    }
    throw new UpdateNotAllowedByUser(user);
  }
}
