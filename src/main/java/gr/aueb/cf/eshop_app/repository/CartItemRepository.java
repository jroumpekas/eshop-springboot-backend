package gr.aueb.cf.eshop_app.repository;

import gr.aueb.cf.eshop_app.models.CartItem;
import gr.aueb.cf.eshop_app.models.Product;
import gr.aueb.cf.eshop_app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUser(User user);

    Optional<CartItem> findByUserAndProduct(User user, Product product);

    void deleteByUser(User user);
}