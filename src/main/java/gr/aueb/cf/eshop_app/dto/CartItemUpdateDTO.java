package gr.aueb.cf.eshop_app.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemUpdateDTO {

    @NotNull(message = "Η ποσότητα είναι υποχρεωτική")
    @Min(value = 1, message = "Η ποσότητα πρέπει να είναι τουλάχιστον 1")
    private Integer quantity;
}