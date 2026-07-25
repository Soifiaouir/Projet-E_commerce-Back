package fr.soifi.ecommerce.dal.repository;

import fr.soifi.ecommerce.dal.entity.Cart;
import fr.soifi.ecommerce.dal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(User user);
}