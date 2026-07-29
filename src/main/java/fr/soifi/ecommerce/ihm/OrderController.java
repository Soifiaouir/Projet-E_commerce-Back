package fr.soifi.ecommerce.ihm;

import fr.soifi.ecommerce.bll.OrderService;
import fr.soifi.ecommerce.bo.OrderDTO;
import fr.soifi.ecommerce.config.CurrentUserProvider;
import fr.soifi.ecommerce.dal.entity.Address;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final CurrentUserProvider currentUserProvider;

    public OrderController(OrderService orderService, CurrentUserProvider currentUserProvider) {
        this.orderService = orderService;
        this.currentUserProvider = currentUserProvider;
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getMyOrders() {
        Long userId = currentUserProvider.getCurrentUserId();
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        Long userId = currentUserProvider.getCurrentUserId();
        return ResponseEntity.ok(orderService.getOrderById(userId, id));
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody Address shippingAddress) {
        Long userId = currentUserProvider.getCurrentUserId();
        OrderDTO created = orderService.createOrderFromCart(userId, shippingAddress);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderDTO> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }
}