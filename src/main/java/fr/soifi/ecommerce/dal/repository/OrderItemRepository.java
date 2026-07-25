package fr.soifi.ecommerce.dal.repository;

import fr.soifi.ecommerce.dal.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}