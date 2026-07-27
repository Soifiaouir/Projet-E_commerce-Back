package fr.soifi.ecommerce.bll;

import fr.soifi.ecommerce.bo.ProductDTO;
import java.util.List;

public interface ProductService {
    List<ProductDTO> getAllProducts();
    ProductDTO getProductById(Long id);
    List<ProductDTO> getProductsByCategory(Long categoryId);
    ProductDTO createProduct(ProductDTO productDTO);
    ProductDTO updateProduct(Long id, ProductDTO productDTO);
    void deleteProduct(Long id);
}