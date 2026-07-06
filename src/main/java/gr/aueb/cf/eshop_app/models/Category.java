package gr.aueb.cf.eshop_app.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "categories")
public class Category extends AbstractEntity {

    @Column(nullable = false, unique = true)
    private String name;


}
