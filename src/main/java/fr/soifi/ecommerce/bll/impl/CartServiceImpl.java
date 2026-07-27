package fr.soifi.ecommerce.bll.impl;

import fr.soifi.ecommerce.bll.CartService;
import fr.soifi.ecommerce.bo.CartDTO;
import fr.soifi.ecommerce.bo.CartItemDTO;
import fr.soifi.ecommerce.bo.ProductDTO;
import fr.soifi.ecommerce.dal.entity.*;
import fr.soifi.ecommerce.dal.repository.CartItemRepository;
import fr.soifi.ecommerce.dal.repository.CartRepository;
import fr.soifi.ecommerce.dal.repository.ProductRepository;
import fr.soifi.ecommerce.dal.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository,
                           ProductRepository productRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CartDTO getCartByUserId(Long userId) {
        Cart cart = findOrCreateCart(userId);
        return toDTO(cart);
    }

    @Override
    public CartDTO addItemToCart(Long userId, Long productId, Integer quantity) {
        Cart cart = findOrCreateCart(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produit introuvable avec l'id " + productId));

        if (product.getStock() < quantity) {
            throw new RuntimeException("Stock insuffisant pour le produit " + product.getName());
        }

        // Si le produit est deja dans le panier, on augmente juste la quantite
        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cartItemRepository.save(newItem);
        }

        Cart refreshed = cartRepository.findById(cart.getId()).orElseThrow();
        return toDTO(refreshed);
    }

    @Override
    public CartDTO updateItemQuantity(Long userId, Long cartItemId, Integer quantity) {
        Cart cart = findOrCreateCart(userId);
        CartItem item = findItemInCart(cart, cartItemId);

        if (item.getProduct().getStock() < quantity) {
            throw new RuntimeException("Stock insuffisant pour le produit " + item.getProduct().getName());
        }

        item.setQuantity(quantity);
        cartItemRepository.save(item);

        Cart refreshed = cartRepository.findById(cart.getId()).orElseThrow();
        return toDTO(refreshed);
    }

    @Override
    public void removeItemFromCart(Long userId, Long cartItemId) {
        Cart cart = findOrCreateCart(userId);
        CartItem item = findItemInCart(cart, cartItemId);
        cartItemRepository.delete(item);
    }

    @Override
    public void clearCart(Long userId) {
        Cart cart = findOrCreateCart(userId);
        cartItemRepository.deleteAll(cart.getItems());
    }

    // --- Methodes utilitaires privees ---

    private Cart findOrCreateCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'id " + userId));

        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }

    private CartItem findItemInCart(Cart cart, Long cartItemId) {
        return cart.getItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item introuvable dans ce panier avec l'id " + cartItemId));
    }

    private CartDTO toDTO(Cart cart) {
        CartDTO dto = new CartDTO();
        dto.setId(cart.getId());
        List<CartItemDTO> itemDTOs = cart.getItems().stream()
                .map(this::toItemDTO)
                .collect(Collectors.toList());
        dto.setItems(itemDTOs);
        return dto;
    }

    private CartItemDTO toItemDTO(CartItem item) {
        CartItemDTO dto = new CartItemDTO();
        dto.setId(item.getId());
        dto.setProduct(toProductDTO(item.getProduct()));
        dto.setQuantity(item.getQuantity());
        return dto;
    }

    private ProductDTO toProductDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setImageUrl(product.getImageUrl());
        dto.setStock(product.getStock());
        return dto;
    }
}