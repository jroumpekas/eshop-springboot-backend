package gr.aueb.cf.eshop_app.service.impl;

import gr.aueb.cf.eshop_app.dto.CartItemInsertDTO;
import gr.aueb.cf.eshop_app.dto.CartItemReadOnlyDTO;
import gr.aueb.cf.eshop_app.dto.CartItemUpdateDTO;
import gr.aueb.cf.eshop_app.mapper.CartItemMapper;
import gr.aueb.cf.eshop_app.models.CartItem;
import gr.aueb.cf.eshop_app.models.Product;
import gr.aueb.cf.eshop_app.models.User;
import gr.aueb.cf.eshop_app.repository.CartItemRepository;
import gr.aueb.cf.eshop_app.repository.ProductRepository;
import gr.aueb.cf.eshop_app.repository.UserRepository;
import gr.aueb.cf.eshop_app.service.CartItemService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartItemMapper cartItemMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CartItemReadOnlyDTO> getCartByUserId(Long userId) {
        User user = getUserOrThrow(userId);

        return cartItemRepository.findByUser(user)
                .stream()
                .map(cartItemMapper::mapToReadOnlyDTO)
                .toList();
    }

    @Override
    public CartItemReadOnlyDTO addItemToCart(Long userId, CartItemInsertDTO dto) {
        User user = getUserOrThrow(userId);

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Product with id " + dto.getProductId() + " was not found"
                ));

        CartItem cartItem = cartItemRepository.findByUserAndProduct(user, product)
                .orElse(null);

        if (cartItem != null) {
            int newQuantity = cartItem.getQuantity() + dto.getQuantity();

            if (newQuantity > product.getStock()) {
                throw new IllegalArgumentException("Not enough stock for product: " + product.getName());
            }

            cartItem.setQuantity(newQuantity);
            CartItem savedCartItem = cartItemRepository.save(cartItem);

            return cartItemMapper.mapToReadOnlyDTO(savedCartItem);
        }

        if (dto.getQuantity() > product.getStock()) {
            throw new IllegalArgumentException("Not enough stock for product: " + product.getName());
        }

        CartItem newCartItem = CartItem.builder()
                .user(user)
                .product(product)
                .quantity(dto.getQuantity())
                .build();

        CartItem savedCartItem = cartItemRepository.save(newCartItem);

        return cartItemMapper.mapToReadOnlyDTO(savedCartItem);
    }

    @Override
    public CartItemReadOnlyDTO updateCartItemQuantity(Long cartItemId, CartItemUpdateDTO dto) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cart item with id " + cartItemId + " was not found"
                ));

        Product product = cartItem.getProduct();

        if (dto.getQuantity() > product.getStock()) {
            throw new IllegalArgumentException("Not enough stock for product: " + product.getName());
        }

        cartItem.setQuantity(dto.getQuantity());

        CartItem updatedCartItem = cartItemRepository.save(cartItem);

        return cartItemMapper.mapToReadOnlyDTO(updatedCartItem);
    }

    @Override
    public void removeCartItem(Long cartItemId) {
        if (!cartItemRepository.existsById(cartItemId)) {
            throw new EntityNotFoundException("Cart item with id " + cartItemId + " was not found");
        }

        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    public void clearCart(Long userId) {
        User user = getUserOrThrow(userId);

        cartItemRepository.deleteByUser(user);
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User with id " + userId + " was not found"
                ));
    }
}