package gr.aueb.cf.eshop_app.service;

import gr.aueb.cf.eshop_app.dto.CartItemInsertDTO;
import gr.aueb.cf.eshop_app.dto.CartItemReadOnlyDTO;
import gr.aueb.cf.eshop_app.dto.CartItemUpdateDTO;

import java.util.List;
import java.util.UUID;

public interface CartItemService {

    List<CartItemReadOnlyDTO> getCartByUserId(UUID userId);

    CartItemReadOnlyDTO addItemToCart(UUID userId, CartItemInsertDTO dto);

    CartItemReadOnlyDTO updateCartItemQuantity(UUID cartItemId, CartItemUpdateDTO dto);

    void removeCartItem(UUID cartItemId);

    void clearCart(UUID userId);
}