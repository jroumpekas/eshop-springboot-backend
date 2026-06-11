package gr.aueb.cf.eshop_app.service;

import gr.aueb.cf.eshop_app.dto.CheckoutRequestDTO;
import gr.aueb.cf.eshop_app.dto.OrderReadOnlyDTO;

import java.util.List;

public interface OrderService {

    List<OrderReadOnlyDTO> getAllOrders();

    OrderReadOnlyDTO getOrderById(Long id);

    List<OrderReadOnlyDTO> getOrdersByUserId(Long userId);

    OrderReadOnlyDTO checkout(Long userId);

    OrderReadOnlyDTO checkout(String username, CheckoutRequestDTO request);

    List<OrderReadOnlyDTO> getMyOrders(String username);
}