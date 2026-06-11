package gr.aueb.cf.eshop_app.service.impl;

import gr.aueb.cf.eshop_app.dto.CheckoutItemDTO;
import gr.aueb.cf.eshop_app.dto.CheckoutRequestDTO;
import gr.aueb.cf.eshop_app.dto.OrderReadOnlyDTO;
import gr.aueb.cf.eshop_app.mapper.OrderMapper;
import gr.aueb.cf.eshop_app.models.CartItem;
import gr.aueb.cf.eshop_app.models.Order;
import gr.aueb.cf.eshop_app.models.OrderItem;
import gr.aueb.cf.eshop_app.models.Product;
import gr.aueb.cf.eshop_app.models.User;
import gr.aueb.cf.eshop_app.repository.CartItemRepository;
import gr.aueb.cf.eshop_app.repository.OrderRepository;
import gr.aueb.cf.eshop_app.repository.ProductRepository;
import gr.aueb.cf.eshop_app.repository.UserRepository;
import gr.aueb.cf.eshop_app.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional(readOnly = true)
    public List<OrderReadOnlyDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::mapToReadOnlyDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderReadOnlyDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order with id " + id + " was not found"
                ));

        return orderMapper.mapToReadOnlyDTO(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderReadOnlyDTO> getOrdersByUserId(Long userId) {
        User user = getUserOrThrow(userId);

        return orderRepository.findByUser(user)
                .stream()
                .map(orderMapper::mapToReadOnlyDTO)
                .toList();
    }

    /**
     * Old checkout method based on backend cart items.
     * You can keep it for now if your controller still uses it.
     */
    @Override
    public OrderReadOnlyDTO checkout(Long userId) {
        User user = getUserOrThrow(userId);

        List<CartItem> cartItems = cartItemRepository.findByUser(user);

        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;

        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .totalAmount(BigDecimal.ZERO)
                .status("COMPLETED")
                .build();

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            if (cartItem.getQuantity() > product.getStock()) {
                throw new IllegalArgumentException(
                        "Not enough stock for product: " + product.getName()
                );
            }

            BigDecimal itemSubtotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            totalAmount = totalAmount.add(itemSubtotal);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .price(product.getPrice().doubleValue())
                    .build();

            orderItems.add(orderItem);

            product.setStock(product.getStock() - cartItem.getQuantity());
        }

        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        cartItemRepository.deleteByUser(user);

        return orderMapper.mapToReadOnlyDTO(savedOrder);
    }

    /**
     * New checkout method based on the logged-in user.
     * The username comes from the JWT Authentication object.
     */
    @Override
    public OrderReadOnlyDTO checkout(String username, CheckoutRequestDTO request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User with username " + username + " was not found"
                ));

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;

        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .totalAmount(BigDecimal.ZERO)
                .status("COMPLETED")
                .build();

        List<OrderItem> orderItems = new ArrayList<>();

        for (CheckoutItemDTO itemDTO : request.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Product with id " + itemDTO.getProductId() + " was not found"
                    ));

            if (itemDTO.getQuantity() > product.getStock()) {
                throw new IllegalArgumentException(
                        "Not enough stock for product: " + product.getName()
                );
            }

            BigDecimal itemSubtotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(itemDTO.getQuantity()));

            totalAmount = totalAmount.add(itemSubtotal);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemDTO.getQuantity())
                    .price(product.getPrice().doubleValue())
                    .build();

            orderItems.add(orderItem);

            product.setStock(product.getStock() - itemDTO.getQuantity());
        }

        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        return orderMapper.mapToReadOnlyDTO(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderReadOnlyDTO> getMyOrders(String username) {
        return orderRepository.findByUserUsernameOrderByOrderDateDesc(username)
                .stream()
                .map(orderMapper::mapToReadOnlyDTO)
                .toList();
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User with id " + userId + " was not found"
                ));
    }
}