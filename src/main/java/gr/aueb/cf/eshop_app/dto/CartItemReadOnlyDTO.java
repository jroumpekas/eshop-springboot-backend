package gr.aueb.cf.eshop_app.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CartItemReadOnlyDTO(
        Long id,
        Long productId,
        String productName,
        BigDecimal productPrice,
        Integer quantity,
        BigDecimal subtotal
)
{}