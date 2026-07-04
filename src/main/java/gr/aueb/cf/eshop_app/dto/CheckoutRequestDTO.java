package gr.aueb.cf.eshop_app.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;


public record CheckoutRequestDTO (

    @NotEmpty(message = "Cart items cannot be empty")
    List<@Valid CheckoutItemDTO> items
) {

}