package gr.aueb.cf.eshop_app.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;


@Builder
public record ProductInsertDTO (


    @NotBlank(message = "Product name is required")
    @Size(max = 100, message = "Product name must be up to 100 characters")
    String name,

    @Size(max = 500, message = "Description must be up to 500 characters")
    String description,

    @NotNull(message = "Price must be included")
    @Positive(message = "Price must be positive")
    BigDecimal price,

    String imageUrl,
    BigDecimal oldPrice,
    String category,
    BigDecimal rating,

    @NotNull(message = "Stock must be included")
    @PositiveOrZero(message = "Stock must be zero or positive")
    Integer stock
) {

}
