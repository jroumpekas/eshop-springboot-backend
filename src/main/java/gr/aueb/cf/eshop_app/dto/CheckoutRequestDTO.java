package gr.aueb.cf.eshop_app.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CheckoutRequestDTO {

    @NotEmpty(message = "Cart items cannot be empty")
    private List<@Valid CheckoutItemDTO> items;
}