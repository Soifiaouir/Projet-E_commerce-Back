package fr.soifi.ecommerce.dal.repository;

import fr.soifi.ecommerce.dal.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}