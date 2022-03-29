package de.mankianer.tresensystem.entities;

import de.mankianer.tresensystem.security.entities.User;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;

@Data
@Entity
@Table(name = "orders")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private OderStatus status = OderStatus.OPEN;

//  private String purchaser;
  @ManyToOne
  @JoinColumn(name = "purchaser_id", foreignKey = @ForeignKey(name = "fk_order_user"))
  private User purchaser;

//  private List<String> products;
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
