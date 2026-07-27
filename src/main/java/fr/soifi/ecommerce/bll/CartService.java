package fr.soifi.ecommerce.bll;

import fr.soifi.ecommerce.bo.CartDTO;

public interface CartService {
    CartDTO getCartByUserId(Long userId);
    CartDTO addItemToCart(Long userId, Long productId, Integer quantity);
    CartDTO updateItemQuantity(Long userId, Long cartItemId, Integer quantity);
    void removeItemFromCart(Long userId, Long cartItemId);
    void clearCart(Long userId);
}