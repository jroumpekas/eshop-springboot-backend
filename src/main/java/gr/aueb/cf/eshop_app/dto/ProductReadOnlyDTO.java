package gr.aueb.cf.eshop_app.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductReadOnlyDTO {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
}
