package fr.soifi.ecommerce.bll.impl;

import fr.soifi.ecommerce.bll.OrderService;
import fr.soifi.ecommerce.bo.OrderDTO;
import fr.soifi.ecommerce.bo.OrderItemDTO;
import fr.soifi.ecommerce.bo.ProductDTO;
import fr.soifi.ecommerce.dal.entity.*;
import fr.soifi.ecommerce.dal.repository.CartItemRepository;
import fr.soifi.ecommerce.dal.repository.CartRepository;
import fr.soifi.ecommerce.dal.repository.OrderRepository;
import fr.soifi.ecommerce.dal.repository.ProductRepository;
import fr.soifi.ecommerce.dal.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderServiceImpl(OrderRepository orderRepository, CartRepository cartRepository,
                            CartItemRepository cartItemRepository, ProductRepository productRepository,
                            UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<OrderDTO> getOrdersByUserId(Long userId) {
        User user = findUser(userId);
        return orderRepository.findByUser(user)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO getOrderById(Long userId, Long orderId) {
        Order order = findOrderBelongingToUser(userId, orderId);
        return toDTO(order);
    }

    @Override
    @Transactional
    public OrderDTO createOrderFromCart(Long userId, Address shippingAddress) {
        User user = findUser(userId);
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Panier introuvable pour cet utilisateur"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Impossible de commander : le panier est vide");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setDateCommande(LocalDateTime.now());
        order.setShippingAddress(shippingAddress);

        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> orderItems = new java.util.ArrayList<>();

        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();

            if (product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("Stock insuffisant pour le produit " + product.getName());
            }

            // On decremente le stock, c'est ici que ca devient definitif
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrixUnitaire(product.getPrice()); // snapshot du prix au moment T
            orderItems.add(orderItem);

            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }

        order.setItems(orderItems);
        order.setMontantTotal(total);

        Order savedOrder = orderRepository.save(order);

        // Le panier est vide une fois la commande passee
        cartItemRepository.deleteAll(cart.getItems());

        return toDTO(savedOrder);
    }

    @Override
    public OrderDTO updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Commande introuvable avec l'id " + orderId));
        order.setStatus(OrderStatus.valueOf(status.toUpperCase()));
        return toDTO(orderRepository.save(order));
    }

    // --- Methodes utilitaires privees ---

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'id " + userId));
    }

    private Order findOrderBelongingToUser(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Commande introuvable avec l'id " + orderId));
        if (!order.getUser().getId().equals(userId)) {
            throw new RuntimeException("Cette commande n'appartient pas a cet utilisateur");
        }
        return order;
    }

    private OrderDTO toDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setStatus(order.getStatus());
        dto.setDateCommande(order.getDateCommande());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setMontantTotal(order.getMontantTotal());
        List<OrderItemDTO> itemDTOs = order.getItems().stream()
                .map(this::toItemDTO)
                .collect(Collectors.toList());
        dto.setItems(itemDTOs);
        return dto;
    }

    private OrderItemDTO toItemDTO(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(item.getId());
        dto.setProduct(toProductDTO(item.getProduct()));
        dto.setQuantity(item.getQuantity());
        dto.setPrixUnitaire(item.getPrixUnitaire());
        return dto;
    }

    private ProductDTO toProductDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setImageUrl(product.getImageUrl());
        return dto;
    }
}