package gr.aueb.cf.eshop_app.dto;

import java.math.BigDecimal;


public record ProductReadOnlyDTO (

        Long id,
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