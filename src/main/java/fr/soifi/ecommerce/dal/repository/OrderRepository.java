package fr.soifi.ecommerce.dal.repository;

import fr.soifi.ecommerce.dal.entity.Order;
import fr.soifi.ecommerce.dal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);
}