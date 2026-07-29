package fr.soifi.ecommerce.ihm;

import fr.soifi.ecommerce.bll.CartService;
import fr.soifi.ecommerce.bo.CartDTO;
import fr.soifi.ecommerce.config.CurrentUserProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final CurrentUserProvider currentUserProvider;

    public CartController(CartService cartService, CurrentUserProvider currentUserProvider) {
        this.cartService = cartService;
        this.currentUserProvider = currentUserProvider;
    }

    @GetMapping
    public ResponseEntity<CartDTO> getMyCart() {
        Long userId = currentUserProvider.getCurrentUserId();
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @PostMapping("/items")
    public ResponseEntity<CartDTO> addItem(@RequestParam Long productId, @RequestParam Integer quantity) {
        Long userId = currentUserProvider.getCurrentUserId();
        return ResponseEntity.ok(cartService.addItemToCart(userId, productId, quantity));
    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<CartDTO> updateItem(@PathVariable Long cartItemId, @RequestParam Integer quantity) {
        Long userId = currentUserProvider.getCurrentUserId();
        return ResponseEntity.ok(cartService.updateItemQuantity(userId, cartItemId, quantity));
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long cartItemId) {
        Long userId = currentUserProvider.getCurrentUserId();
        cartService.removeItemFromCart(userId, cartItemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart() {
        Long userId = currentUserProvider.getCurrentUserId();
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}