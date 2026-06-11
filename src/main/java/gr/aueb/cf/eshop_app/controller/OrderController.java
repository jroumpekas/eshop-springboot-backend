package gr.aueb.cf.eshop_app.controller;

import gr.aueb.cf.eshop_app.dto.CheckoutRequestDTO;
import gr.aueb.cf.eshop_app.dto.OrderReadOnlyDTO;
import gr.aueb.cf.eshop_app.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderReadOnlyDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrderReadOnlyDTO> checkout(
            @Valid @RequestBody CheckoutRequestDTO request,
            Authentication authentication
    ) {
        String username = authentication.getName();

        OrderReadOnlyDTO order = orderService.checkout(username, request);

        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderReadOnlyDTO>> getMyOrders(Authentication authentication) {
        String username = authentication.getName();

        List<OrderReadOnlyDTO> orders = orderService.getMyOrders(username);

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderReadOnlyDTO> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderReadOnlyDTO>> getOrdersByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }

    @PostMapping("/checkout/{userId}")
    public ResponseEntity<OrderReadOnlyDTO> checkout(@PathVariable Long userId) {
        OrderReadOnlyDTO order = orderService.checkout(userId);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
}