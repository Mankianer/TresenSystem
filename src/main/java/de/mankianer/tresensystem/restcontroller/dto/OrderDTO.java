package de.mankianer.tresensystem.restcontroller.dto;

import de.mankianer.tresensystem.entities.Order;
import de.mankianer.tresensystem.security.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderDTO {

    private Long id;
    private Order.OderStatus status = Order.OderStatus.OPEN;
    private User purchaser;
    private List<ProductDTO> products;
    private LocalDateTime createdAt;

    private boolean createdByUser;

    public OrderDTO(Order order) {
        this.id = order.getId();
        this.status = order.getStatus();
        this.purchaser = order.getPurchaser();
        this.products = order.getProducts().stream().map(ProductDTO::new).toList();
        this.createdAt = order.getCreatedAt();
    }

    public Order toOrder() {
        Order order = new Order();
        order.setId(id);
        order.setStatus(status);
        order.setPurchaser(purchaser);
        order.setProducts(products.stream().map(ProductDTO::toProduct).toList());
        order.setCreatedAt(createdAt);
        return order;
    }
}
