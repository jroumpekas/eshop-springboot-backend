package gr.aueb.cf.eshop_app.dto;


import lombok.Builder;


@Builder
public record AuthResponseDTO (

    String token,
    Long userId,
    String username,
    String email
) {

}
