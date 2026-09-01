package gr.aueb.cf.eshop_app.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record CartItemReadOnlyDTO(

        UUID id,
        UUID productId,
        String productName,
        BigDecimal productPrice,
        Integer quantity,
        BigDecimal subtotal
) {

}