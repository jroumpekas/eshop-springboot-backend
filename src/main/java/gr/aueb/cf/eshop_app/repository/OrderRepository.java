package gr.aueb.cf.eshop_app.repository;

import gr.aueb.cf.eshop_app.models.Order;
import gr.aueb.cf.eshop_app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findByUser(User user);

    List<Order> findByStatus (String status);

    List<Order> findByUserUsernameOrderByOrderDateDesc(String username);
}
