package gr.aueb.cf.eshop_app.repository;


import gr.aueb.cf.eshop_app.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProductRepository  extends JpaRepository<Product, UUID> {
    List<Product> findByNameContainingIgnoreCase(String name);
}
