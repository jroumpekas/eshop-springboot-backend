package gr.aueb.cf.eshop_app.dto;

import lombok.Builder;

@Builder
public record OrderItemReadOnlyDTO (

        Long id,
        Long productId,
        String productName,
        Integer quantity,
        Double price

)
{}