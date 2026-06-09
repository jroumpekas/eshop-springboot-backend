package gr.aueb.cf.eshop_app.mapper;

import gr.aueb.cf.eshop_app.dto.OrderItemReadOnlyDTO;
import gr.aueb.cf.eshop_app.dto.OrderReadOnlyDTO;
import gr.aueb.cf.eshop_app.models.Order;
import gr.aueb.cf.eshop_app.models.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {

    public OrderReadOnlyDTO mapToReadOnlyDTO(Order order) {
        List<OrderItemReadOnlyDTO> orderItemDTOs = order.getOrderItems()
                .stream()
                .map(this::mapOrderItemToReadOnlyDTO)
                .toList();

        return OrderReadOnlyDTO.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .username(order.getUser().getUsername())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .orderItems(orderItemDTOs)
                .build();
    }

    private OrderItemReadOnlyDTO mapOrderItemToReadOnlyDTO(OrderItem orderItem) {
        return OrderItemReadOnlyDTO.builder()
                .id(orderItem.getId())
                .productId(orderItem.getProduct().getId())
                .productName(orderItem.getProduct().getName())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .build();
    }
}