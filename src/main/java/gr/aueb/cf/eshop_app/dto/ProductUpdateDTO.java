package gr.aueb.cf.eshop_app.dto;


import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductUpdateDTO {

    @NotBlank(message = "Product name is required")
    @Size(max = 100, message = "Product name must be up to 100 characters")
    private String name;

    @Size(max = 500, message = "Description must be up to 500 characters")
    private String description;

    @NotNull(message = "Price must be included")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    @NotNull(message = "Stock must be included")
    @PositiveOrZero(message = "Stock must be zero or positive")
    private Integer stock;

    private String imageUrl;
    private BigDecimal oldPrice;
    private String category;
    private BigDecimal rating;
}
