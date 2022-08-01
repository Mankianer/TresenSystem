package de.mankianer.tresensystem.restcontroller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.util.Map;

/**
 * DTO for receiving updates of an order.
 */
@Data
@NoArgsConstructor
public class UpdateOrderDTO {

    @Nullable
    private Long id;
    @NonNull
    private String purchaser;
    @NonNull
    private Map<Long, Integer> positions;

}
