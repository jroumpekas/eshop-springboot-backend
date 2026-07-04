package gr.aueb.cf.eshop_app.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record OrderReadOnlyDTO(
        Long id,
        Long userId,
        String username,
        LocalDateTime orderDate,
        BigDecimal totalAmount,
        String status,
        List<OrderItemReadOnlyDTO> orderItems
) {

}