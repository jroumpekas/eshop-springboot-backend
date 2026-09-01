package gr.aueb.cf.eshop_app.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record OrderReadOnlyDTO(
        UUID id,
        UUID userId,
        String username,
        LocalDateTime orderDate,
        BigDecimal totalAmount,
        String status,
        List<OrderItemReadOnlyDTO> orderItems
) {

}