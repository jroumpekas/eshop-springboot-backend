package gr.aueb.cf.eshop_app.dto;

import java.math.BigDecimal;
import java.util.UUID;


public record ProductReadOnlyDTO (

        UUID id,
        String name,
        String description,
        BigDecimal price,
        Integer stock,

        String imageUrl,
        BigDecimal oldPrice,
        String category,
        BigDecimal rating
)
{}