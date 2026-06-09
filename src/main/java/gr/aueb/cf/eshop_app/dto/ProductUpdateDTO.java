package gr.aueb.cf.eshop_app.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
    @Positive(message = "Stock must be positive")
    private Integer stock;
}
