package gr.aueb.cf.eshop_app.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryReadOnlyDTO {

    private Long id;
    private String name;
}