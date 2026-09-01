package gr.aueb.cf.eshop_app.dto;

import lombok.Builder;

import java.util.UUID;


@Builder
public record CategoryReadOnlyDTO (

        UUID id,
        String name
) {

}