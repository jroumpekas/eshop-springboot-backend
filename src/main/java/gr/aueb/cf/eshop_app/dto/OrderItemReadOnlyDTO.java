package gr.aueb.cf.eshop_app.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemReadOnlyDTO {

    private Long id;
    private Long productId;
    private String productName;
    private Integer quantity;
    private Double price;
}