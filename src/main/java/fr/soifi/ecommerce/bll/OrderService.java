package fr.soifi.ecommerce.bll;

import fr.soifi.ecommerce.bo.OrderDTO;
import fr.soifi.ecommerce.dal.entity.Address;
import java.util.List;

public interface OrderService {
    List<OrderDTO> getOrdersByUserId(Long userId);
    OrderDTO getOrderById(Long userId, Long orderId);
    OrderDTO createOrderFromCart(Long userId, Address shippingAddress);
    OrderDTO updateOrderStatus(Long orderId, String status);
}