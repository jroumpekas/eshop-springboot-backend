package gr.aueb.cf.eshop_app.mapper;

import gr.aueb.cf.eshop_app.dto.CartItemReadOnlyDTO;
import gr.aueb.cf.eshop_app.models.CartItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CartItemMapper {

    public CartItemReadOnlyDTO mapToReadOnlyDTO(CartItem cartItem) {
        BigDecimal subtotal = cartItem.getProduct()
                .getPrice()
                .multiply(BigDecimal.valueOf(cartItem.getQuantity()));

        return CartItemReadOnlyDTO.builder()
                .id(cartItem.getId())
                .productId(cartItem.getProduct().getId())
                .productName(cartItem.getProduct().getName())
                .productPrice(cartItem.getProduct().getPrice())
                .quantity(cartItem.getQuantity())
                .subtotal(subtotal)
                .build();
    }
}