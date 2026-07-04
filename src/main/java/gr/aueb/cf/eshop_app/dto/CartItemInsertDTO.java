package gr.aueb.cf.eshop_app.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;


@Builder
public record CartItemInsertDTO (

        @NotNull(message = "Το productId είναι υποχρεωτικό")
        Long productId,

        @NotNull(message = "Η ποσότητα είναι υποχρεωτική")
        @Min(value = 1, message = "Η ποσότητα πρέπει να είναι τουλάχιστον 1")
        Integer quantity

) {
}