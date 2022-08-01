package de.mankianer.tresensystem.restcontroller.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import de.mankianer.tresensystem.entities.Order;
import de.mankianer.tresensystem.security.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class OrderDTO {

    private Long id;
    private User purchaser;
    private Map<ProductDTO, Integer> positions;
    private LocalDateTime createdAt;

    private boolean createdByUser;

    private LocalDateTime canceledAt;

    public OrderDTO(Order order) {
        this.id = order.getId();
        this.purchaser = order.getPurchaser();
        this.positions = DTO_Utils.OrderToDtoOrderPosition(order);
        this.createdAt = order.getCreatedAt();
        this.createdByUser = order.isCreatedByUser();
        this.canceledAt = order.getCanceledAt();
    }

    public Order toOrder() {
        Order order = new Order();
        order.setId(id);
        order.setPurchaser(purchaser);
        order.setPositions(DTO_Utils.addOrderPositionFromOrderDTO(order, this));
        order.setCreatedAt(createdAt);
        order.setCreatedByUser(createdByUser);
        order.setCanceledAt(canceledAt);
        return order;
    }

    @JsonGetter("positions")
    public Map<Long, Integer> getJsonPositions() {
        return positions.entrySet().stream()
                .map(e -> Map.entry(e.getKey().getId(), e.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
