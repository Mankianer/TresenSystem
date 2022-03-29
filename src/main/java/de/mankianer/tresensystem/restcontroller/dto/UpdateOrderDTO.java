package de.mankianer.tresensystem.restcontroller.dto;

import de.mankianer.tresensystem.entities.Order.OderStatus;
import de.mankianer.tresensystem.entities.Product;
import de.mankianer.tresensystem.security.entities.User;
import java.util.List;
import lombok.Data;

@Data
public class UpdateOrderDTO {
  private OderStatus status = OderStatus.OPEN;
  private User purchaser;
  private List<Long> products;
}
