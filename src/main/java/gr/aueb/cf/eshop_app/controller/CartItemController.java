package gr.aueb.cf.eshop_app.controller;

import gr.aueb.cf.eshop_app.dto.CartItemInsertDTO;
import gr.aueb.cf.eshop_app.dto.CartItemReadOnlyDTO;
import gr.aueb.cf.eshop_app.dto.CartItemUpdateDTO;
import gr.aueb.cf.eshop_app.service.CartItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItemReadOnlyDTO>> getCartByUserId(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(cartItemService.getCartByUserId(userId));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<CartItemReadOnlyDTO> addItemToCart(
            @PathVariable Long userId,
            @Valid @RequestBody CartItemInsertDTO dto
    ) {
        CartItemReadOnlyDTO cartItem = cartItemService.addItemToCart(userId, dto);
        return new ResponseEntity<>(cartItem, HttpStatus.CREATED);
    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<CartItemReadOnlyDTO> updateCartItemQuantity(
            @PathVariable Long cartItemId,
            @Valid @RequestBody CartItemUpdateDTO dto
    ) {
        return ResponseEntity.ok(cartItemService.updateCartItemQuantity(cartItemId, dto));
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> removeCartItem(
            @PathVariable Long cartItemId
    ) {
        cartItemService.removeCartItem(cartItemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> clearCart(
            @PathVariable Long userId
    ) {
        cartItemService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}