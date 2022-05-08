package de.mankianer.tresensystem.entities;

import de.mankianer.tresensystem.security.entities.User;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private OderStatus status = OderStatus.OPEN;

  @ManyToOne
  @JoinColumn(name = "purchaser_id", foreignKey = @ForeignKey(name = "fk_order_user"))
  private User purchaser;

  @ManyToMany
  @JoinTable(name = "order_products", joinColumns = @JoinColumn(name = "order_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
  private List<Product> products;

  private LocalDateTime createdAt;

  private boolean createdByUser;

  public enum OderStatus {
    OPEN,
    CLOSED,
    CANCELED,
    IN_PROGRESS
  }
}
