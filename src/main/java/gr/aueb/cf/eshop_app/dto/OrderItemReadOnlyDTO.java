package gr.aueb.cf.eshop_app.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record OrderItemReadOnlyDTO (

        UUID id,
        UUID productId,
        String productName,
        Integer quantity,
        Double price

)
{

}