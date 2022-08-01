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

  @ManyToOne
  @JoinColumn(name = "purchaser_id", foreignKey = @ForeignKey(name = "fk_order_user"))
  private User purchaser;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumns({
          @JoinColumn(name = "order_id", referencedColumnName = "id"),
  })
  private List<OrderPosition> positions;

  private LocalDateTime createdAt;

  private boolean createdByUser;

  private LocalDateTime canceledAt;

  public boolean isCanceled() {
    return canceledAt != null;
  }
}
