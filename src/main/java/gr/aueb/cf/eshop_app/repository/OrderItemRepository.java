package gr.aueb.cf.eshop_app.repository;

import gr.aueb.cf.eshop_app.models.Order;
import gr.aueb.cf.eshop_app.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrder(Order order);

}