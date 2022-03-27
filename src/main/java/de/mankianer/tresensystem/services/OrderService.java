package de.mankianer.tresensystem.services;

import de.mankianer.tresensystem.entities.Order;
import de.mankianer.tresensystem.exeptions.order.MissingPurchaserException;
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
   * @param user the user who wants to get the orders
   * @return the list of orders of the user or all orders if the user is a barkeeper
   */
  public List<Order> getOrders(User user) {
    if (user.getAuthorities().contains(AuthorityEnum.BARKEEPER)) {
      return orderRepository.findAllByCreatedAtAfter(LocalDateTime.now().minusHours(2));
    }
    return orderRepository.findAllByPurchaser(user);
  }

  /**
   * @param user the user who wants to create the order
   * @param order the order to create if the user is not a barkeeper the purchase is set to the user
   * except if the user is Barkeeper and the purchase is set
   * @return the new order
   */
  public Order createOrder(User user, Order order) {
    if (!user.getAuthorities().contains(AuthorityEnum.BARKEEPER) || order.getPurchaser() == null) {
      order.setPurchaser(user);
    }
    return orderRepository.save(order);
  }

  /**
   * @param user the user who wants to get the order if the user is not a barkeeper the order have
   * to be of the user
   * @param id the id of the order
   * @return the order
   * @throws OrderNotFound if the order is not found
   */
  public Order getOrder(User user, Long id) throws OrderNotFound {
    if (user.getAuthorities().contains(AuthorityEnum.BARKEEPER)) {
      return orderRepository.findById(id).orElseThrow(() -> new OrderNotFound("id: " + id));
    } else {
      return orderRepository.findByIdAndPurchaser(id, user)
          .orElseThrow(() -> new OrderNotFound("id: " + id + " and user: " + user));
    }
  }

  /**
   * @param order the order to update
   * @param user the user who wants to update the order
   * @return the updated order
   * @throws UpdateNotAllowedByUser if the order is not of the user or the user is a barkeeper and
   * the purchase is not equal
   * @throws OrderNotFound if the order is not found
   */
  public Order updateOrder(User user, Order order) throws OrderNotFound, UpdateNotAllowedByUser {
    Order orderToUpdate = orderRepository.findById(order.getId())
        .orElseThrow(() -> new OrderNotFound("id: " + order.getId()));
    if (orderToUpdate.getPurchaser().equals(user)) {
      return orderRepository.save(order);
    } else if (user.getAuthorities().contains(AuthorityEnum.BARKEEPER)
        && orderToUpdate.getPurchaser().equals(order.getPurchaser())) {
      return orderRepository.save(order);
    }
    throw new UpdateNotAllowedByUser(user);
  }
}
