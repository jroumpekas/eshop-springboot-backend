package gr.aueb.cf.eshop_app.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UserReadOnlyDTO (

        UUID id,
        String username,
        String email,
        String firstName,
        String lastName,
        String role

) {

}
