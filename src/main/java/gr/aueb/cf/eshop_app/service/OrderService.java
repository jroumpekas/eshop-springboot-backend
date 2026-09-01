package gr.aueb.cf.eshop_app.service;

import gr.aueb.cf.eshop_app.dto.CheckoutRequestDTO;
import gr.aueb.cf.eshop_app.dto.OrderReadOnlyDTO;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    List<OrderReadOnlyDTO> getAllOrders();

    OrderReadOnlyDTO getOrderById(UUID id);

    List<OrderReadOnlyDTO> getOrdersByUserId(UUID userId);

    OrderReadOnlyDTO checkout(UUID userId);

    OrderReadOnlyDTO checkout(String username, CheckoutRequestDTO request);

    List<OrderReadOnlyDTO> getMyOrders(String username);
}