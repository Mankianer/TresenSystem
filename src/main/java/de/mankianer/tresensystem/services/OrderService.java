package de.mankianer.tresensystem.services;

import de.mankianer.tresensystem.entities.Order;
import de.mankianer.tresensystem.entities.OrderPosition;
import de.mankianer.tresensystem.entities.OrderPositionID;
import de.mankianer.tresensystem.exeptions.ProductNotFoundException;
import de.mankianer.tresensystem.exeptions.UserMisMatchingException;
import de.mankianer.tresensystem.exeptions.UserNotFoundException;
import de.mankianer.tresensystem.exeptions.order.MissingValueException;
import de.mankianer.tresensystem.exeptions.order.OrderNotFound;
import de.mankianer.tresensystem.repository.OrderRepository;
import de.mankianer.tresensystem.repository.ProductRepository;
import de.mankianer.tresensystem.restcontroller.dto.UpdateOrderDTO;
import de.mankianer.tresensystem.security.entities.Authority.AuthorityEnum;
import de.mankianer.tresensystem.security.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final ProductRepository productRepository;

  @Autowired
  private UserService userService;

  @Autowired
  private ProductService productService;

  public OrderService(OrderRepository orderRepository,
                      ProductRepository productRepository) {
    this.orderRepository = orderRepository;
    this.productRepository = productRepository;
  }

  /**
   * @return the list of orders of the user
   */
  public List<Order> getOrdersByUsern(User user) {
    return orderRepository.findAllByPurchaser(user);
  }

  /**
   * @param pastHours the hours in past to get the orders from
   * @return the list of orders
   */
  public List<Order> getOrdersPastTime(int pastHours) {
    return orderRepository.findAllByCreatedAtAfter(LocalDateTime.now().minusHours(pastHours));
  }

  /**
   * @param user the user who wants the order
   * @param order the order to create
   * @return the new order
   */
  public Order createOrderbyUser(User user, Order order) {
    order.setPurchaser(user);
    order.setCreatedByUser(true);
    //ToDo validate order
    return orderRepository.save(order);
  }

  /**
   * @param order the order to create
   * @return the new order
   */
  public Order createOrderByBarkeeper(Order order) throws MissingValueException {
    if (order.getPurchaser() == null) {
      throw new MissingValueException("purchaser is needed");
    }
    order.setCreatedByUser(false);
    //ToDo validate order
    return orderRepository.save(order);
  }

  /**
   * @param user the user who wants to get the order
   * @param id the id of the order
   * @return the order
   * @throws OrderNotFound if the order is not found
   */
  public Order getOrderByUserAndId(User user, Long id) throws OrderNotFound {
    if (user.getAuthorities().contains(AuthorityEnum.BARKEEPER.name())) {
      return orderRepository.findById(id).orElseThrow(() -> new OrderNotFound("id: " + id));
    } else {
      return orderRepository.findByIdAndPurchaser(id, user)
          .orElseThrow(() -> new OrderNotFound("id: " + id + " and user: " + user));
    }
  }

  /**
   * @param id the id of the order
   * @return the order
   * @throws OrderNotFound if the order is not found
   */
  public Order getOrderById(Long id) throws OrderNotFound {
    return orderRepository.findById(id).orElseThrow(() -> new OrderNotFound("id: " + id));
  }

  /**
   * @param order the order to update
   * @return the updated order
   * @throws OrderNotFound if the order is not found by id and user
   */
  public Order updateOrder(Order order) throws OrderNotFound {
    orderRepository.existsById(order.getId());
    return orderRepository.save(order);
  }

  /**
   * Convert an UpdateOrderDTO to an Order
   * and validates the order
   *
   * @param updateOrderDTO
   * @return
   * @throws UserNotFoundException
   * @throws ProductNotFoundException
   * @throws OrderNotFound
   * @throws UserMisMatchingException
   */
  public Order getOrderFromUpdateOrderDTO(UpdateOrderDTO updateOrderDTO) throws UserNotFoundException, ProductNotFoundException, OrderNotFound, UserMisMatchingException {
    Order order;
    if (updateOrderDTO.getId() == null) {
      order = new Order();
      User user = userService.findUser(updateOrderDTO.getPurchaser());
      order.setPurchaser(user);
      setPositionFromUpdate(updateOrderDTO, order);
      order.setCreatedAt(LocalDateTime.now());
      order.setCreatedByUser(false);

    } else {
      order = getOrderById(updateOrderDTO.getId());
      if (!order.getPurchaser().getUsername().equals(updateOrderDTO.getPurchaser())) {
        throw new UserMisMatchingException(order.getPurchaser().getUsername(), updateOrderDTO.getPurchaser());
      }
      setPositionFromUpdate(updateOrderDTO, order);
    }
    return order;
  }

  private void setPositionFromUpdate(UpdateOrderDTO updateOrderDTO, Order order) throws ProductNotFoundException {
    try {
      order.setPositions(updateOrderDTO.getPositions().entrySet().stream()
              .map(e -> Map.entry(productRepository.findById(e.getKey()).orElse(null), e.getValue()))
              .map(e -> new OrderPosition(new OrderPositionID(order, e.getKey()), e.getValue())).toList());
    } catch (NullPointerException e) {
      throw new ProductNotFoundException(String.format("product not found! %s", updateOrderDTO.getPositions()));
    }
  }
}
