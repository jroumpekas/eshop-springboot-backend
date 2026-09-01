package gr.aueb.cf.eshop_app.dto;


import lombok.Builder;

import java.util.UUID;


@Builder
public record AuthResponseDTO (

    String token,
    UUID userId,
    String username,
    String email
) {

}
