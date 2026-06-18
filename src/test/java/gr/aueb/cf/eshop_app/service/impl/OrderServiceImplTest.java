package gr.aueb.cf.eshop_app.service.impl;

import gr.aueb.cf.eshop_app.dto.CheckoutItemDTO;
import gr.aueb.cf.eshop_app.dto.CheckoutRequestDTO;
import gr.aueb.cf.eshop_app.dto.OrderReadOnlyDTO;
import gr.aueb.cf.eshop_app.mapper.OrderMapper;
import gr.aueb.cf.eshop_app.models.Order;
import gr.aueb.cf.eshop_app.models.Product;
import gr.aueb.cf.eshop_app.models.User;
import gr.aueb.cf.eshop_app.repository.CartItemRepository;
import gr.aueb.cf.eshop_app.repository.OrderRepository;
import gr.aueb.cf.eshop_app.repository.ProductRepository;
import gr.aueb.cf.eshop_app.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void checkoutWithValidRequestCreatesOrderAndReducesStock() {
        // Arrange
        String username = "admin";

        User user = createUser(1L, username);
        Product product = createProduct(10L, "Laptop Lenovo", new BigDecimal("100.00"), 5);

        CheckoutRequestDTO request = createCheckoutRequest(10L, 2);

        OrderReadOnlyDTO expectedDTO = OrderReadOnlyDTO.builder()
                .id(1L)
                .userId(1L)
                .username(username)
                .totalAmount(new BigDecimal("200.00"))
                .status("COMPLETED")
                .orderItems(List.of())
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderMapper.mapToReadOnlyDTO(any(Order.class))).thenReturn(expectedDTO);

        // Act
        OrderReadOnlyDTO result = orderService.checkout(username, request);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDTO, result);

        assertEquals(3, product.getStock());

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());

        Order savedOrder = orderCaptor.getValue();

        assertEquals(user, savedOrder.getUser());
        assertEquals("COMPLETED", savedOrder.getStatus());
        assertEquals(0, new BigDecimal("200.00").compareTo(savedOrder.getTotalAmount()));

        assertNotNull(savedOrder.getOrderItems());
        assertEquals(1, savedOrder.getOrderItems().size());

        assertEquals(product, savedOrder.getOrderItems().get(0).getProduct());
        assertEquals(2, savedOrder.getOrderItems().get(0).getQuantity());
        assertEquals(100.00, savedOrder.getOrderItems().get(0).getPrice());

        verify(userRepository).findByUsername(username);
        verify(productRepository).findById(10L);
        verify(orderRepository).save(any(Order.class));
        verify(orderMapper).mapToReadOnlyDTO(any(Order.class));
    }

    @Test
    void checkoutWithEmptyItemsThrowsException() {
        // Arrange
        String username = "admin";
        User user = createUser(1L, username);

        CheckoutRequestDTO request = new CheckoutRequestDTO();
        request.setItems(List.of());

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> orderService.checkout(username, request)
        );

        // Assert
        assertEquals("Cart is empty", exception.getMessage());

        verify(userRepository).findByUsername(username);
        verifyNoInteractions(productRepository);
        verify(orderRepository, never()).save(any(Order.class));
        verifyNoInteractions(orderMapper);
    }

    @Test
    void checkoutWithQuantityGreaterThanStockThrowsException() {
        // Arrange
        String username = "admin";

        User user = createUser(1L, username);
        Product product = createProduct(10L, "Laptop Lenovo", new BigDecimal("100.00"), 3);

        CheckoutRequestDTO request = createCheckoutRequest(10L, 10);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> orderService.checkout(username, request)
        );

        // Assert
        assertEquals("Not enough stock for product: Laptop Lenovo", exception.getMessage());
        assertEquals(3, product.getStock());

        verify(userRepository).findByUsername(username);
        verify(productRepository).findById(10L);
        verify(orderRepository, never()).save(any(Order.class));
        verifyNoInteractions(orderMapper);
    }

    @Test
    void checkoutWithUnknownUserThrowsEntityNotFoundException() {
        // Arrange
        String username = "unknown-user";
        CheckoutRequestDTO request = createCheckoutRequest(10L, 1);

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> orderService.checkout(username, request)
        );

        // Assert
        assertEquals("User with username unknown-user was not found", exception.getMessage());

        verify(userRepository).findByUsername(username);
        verifyNoInteractions(productRepository);
        verify(orderRepository, never()).save(any(Order.class));
        verifyNoInteractions(orderMapper);
    }

    @Test
    void checkoutWithUnknownProductThrowsEntityNotFoundException() {
        // Arrange
        String username = "admin";

        User user = createUser(1L, username);
        CheckoutRequestDTO request = createCheckoutRequest(99L, 1);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> orderService.checkout(username, request)
        );

        // Assert
        assertEquals("Product with id 99 was not found", exception.getMessage());

        verify(userRepository).findByUsername(username);
        verify(productRepository).findById(99L);
        verify(orderRepository, never()).save(any(Order.class));
        verifyNoInteractions(orderMapper);
    }

    @Test
    void getMyOrdersReturnsMappedOrders() {
        // Arrange
        String username = "admin";

        User user = createUser(1L, username);

        Order order = Order.builder()
                .id(1L)
                .user(user)
                .status("COMPLETED")
                .totalAmount(new BigDecimal("100.00"))
                .orderItems(List.of())
                .build();

        OrderReadOnlyDTO orderDTO = OrderReadOnlyDTO.builder()
                .id(1L)
                .userId(1L)
                .username(username)
                .status("COMPLETED")
                .totalAmount(new BigDecimal("100.00"))
                .orderItems(List.of())
                .build();

        when(orderRepository.findByUserUsernameOrderByOrderDateDesc(username))
                .thenReturn(List.of(order));

        when(orderMapper.mapToReadOnlyDTO(order)).thenReturn(orderDTO);

        // Act
        List<OrderReadOnlyDTO> result = orderService.getMyOrders(username);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(orderDTO, result.get(0));

        verify(orderRepository).findByUserUsernameOrderByOrderDateDesc(username);
        verify(orderMapper).mapToReadOnlyDTO(order);
    }

    private User createUser(Long id, String username) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(username + "@test.com");
        user.setPassword("encoded-password");
        user.setFirstName("Test");
        user.setLastName("User");
        return user;
    }

    private Product createProduct(Long id, String name, BigDecimal price, Integer stock) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setDescription("Test product description");
        product.setPrice(price);
        product.setStock(stock);
        return product;
    }

    private CheckoutRequestDTO createCheckoutRequest(Long productId, Integer quantity) {
        CheckoutItemDTO itemDTO = new CheckoutItemDTO();
        itemDTO.setProductId(productId);
        itemDTO.setQuantity(quantity);

        CheckoutRequestDTO request = new CheckoutRequestDTO();
        request.setItems(List.of(itemDTO));

        return request;
    }
}