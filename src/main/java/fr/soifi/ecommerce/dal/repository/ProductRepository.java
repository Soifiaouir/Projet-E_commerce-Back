package fr.soifi.ecommerce.dal.repository;

import fr.soifi.ecommerce.dal.entity.Product;
import fr.soifi.ecommerce.dal.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(Category category);

    List<Product> findByNameContainingIgnoreCase(String keyword);
}