package de.mankianer.tresensystem.restcontroller.dto;

import de.mankianer.tresensystem.entities.Order;
import de.mankianer.tresensystem.entities.OrderPosition;
import de.mankianer.tresensystem.entities.OrderPositionID;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DTO_Utils {

    public static Map<ProductDTO, Integer> OrderToDtoOrderPosition(Order order) {
        return order.getPositions().stream().map(p -> Map.entry(new ProductDTO(p.getId().getProduct()), p.getAmount())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static List<OrderPosition> addOrderPositionFromOrderDTO(Order order, OrderDTO orderDTO) {
        return orderDTO.getPositions().entrySet().stream().map(e -> new OrderPosition(new OrderPositionID(order, e.getKey().toProduct()), e.getValue())).collect(Collectors.toList());
    }
}
