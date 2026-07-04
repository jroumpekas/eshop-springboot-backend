package gr.aueb.cf.eshop_app.dto;

import lombok.Builder;


@Builder
public record CategoryReadOnlyDTO (
        Long id,
        String name
)

{}