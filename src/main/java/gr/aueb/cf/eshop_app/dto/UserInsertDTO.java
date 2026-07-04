package gr.aueb.cf.eshop_app.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;


@Builder
public record UserInsertDTO (

    @NotBlank(message = "Το username είναι υποχρεωτικό")
    @Size(min = 2, max = 50, message = "Το username πρέπει να είναι μεταξύ 2 και 50 χαρακτήρων")
    String username,

    @NotBlank(message = "Το email είναι υποχρεωτικό")
    @Email(message = "Το email δεν είναι έγκυρο")
    @Size(max = 100, message = "Το email δεν μπορεί να ξεπερνά τους 100 χαρακτήρες")
    String email,

    @NotBlank(message = "Το password είναι υποχρεωτικό")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Το password πρέπει να έχει τουλάχιστον 8 χαρακτήρες, 1 πεζό, 1 κεφαλαίο, 1 ψηφίο, 1 ειδικό χαρακτήρα και να μην έχει κενά"
    )
    String password,

    @NotBlank(message = "Το όνομα είναι υποχρεωτικό")
    @Size(max = 50, message = "Το όνομα δεν μπορεί να ξεπερνά τους 50 χαρακτήρες")
    String firstName,

    @NotBlank(message = "Το επώνυμο είναι υποχρεωτικό")
    @Size(max = 50, message = "Το επώνυμο δεν μπορεί να ξεπερνά τους 50 χαρακτήρες")
    String lastName

) {

}