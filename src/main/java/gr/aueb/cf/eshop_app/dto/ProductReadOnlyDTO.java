package gr.aueb.cf.eshop_app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class ProductReadOnlyDTO {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;

    private String imageUrl;
    private BigDecimal oldPrice;
    private String category;
    private BigDecimal rating;
}