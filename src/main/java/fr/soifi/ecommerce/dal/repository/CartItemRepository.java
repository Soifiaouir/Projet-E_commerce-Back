package fr.soifi.ecommerce.dal.repository;

import fr.soifi.ecommerce.dal.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}