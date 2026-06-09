package gr.aueb.cf.eshop_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryUpdateDTO {

    @NotBlank(message = "Το όνομα κατηγορίας είναι υποχρεωτικό")
    @Size(max = 255, message = "Το όνομα κατηγορίας δεν μπορεί να ξεπερνά τους 255 χαρακτήρες")
    private String name;
}