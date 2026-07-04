package gr.aueb.cf.eshop_app.dto;

import lombok.Builder;

@Builder
public record UserReadOnlyDTO (

        Long id,
        String username,
        String email,
        String firstName,
        String lastName,
        String role

)

{}
