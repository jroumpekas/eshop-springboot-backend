package gr.aueb.cf.eshop_app.service;

import gr.aueb.cf.eshop_app.dto.CartItemInsertDTO;
import gr.aueb.cf.eshop_app.dto.CartItemReadOnlyDTO;
import gr.aueb.cf.eshop_app.dto.CartItemUpdateDTO;

import java.util.List;

public interface CartItemService {

    List<CartItemReadOnlyDTO> getCartByUserId(Long userId);

    CartItemReadOnlyDTO addItemToCart(Long userId, CartItemInsertDTO dto);

    CartItemReadOnlyDTO updateCartItemQuantity(Long cartItemId, CartItemUpdateDTO dto);

    void removeCartItem(Long cartItemId);

    void clearCart(Long userId);
}
