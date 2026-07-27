package fr.soifi.ecommerce.bll.impl;

import fr.soifi.ecommerce.bll.ProductService;
import fr.soifi.ecommerce.bo.CategoryDTO;
import fr.soifi.ecommerce.bo.ProductDTO;
import fr.soifi.ecommerce.dal.entity.Category;
import fr.soifi.ecommerce.dal.entity.Product;
import fr.soifi.ecommerce.dal.repository.CategoryRepository;
import fr.soifi.ecommerce.dal.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO getProductById(Long id) {
        return toDTO(findEntityById(id));
    }

    @Override
    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        Category category = findCategoryById(categoryId);
        return productRepository.findByCategory(category)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = toEntity(productDTO);
        return toDTO(productRepository.save(product));
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product existing = findEntityById(id);
        existing.setName(productDTO.getName());
        existing.setDescription(productDTO.getDescription());
        existing.setPrice(productDTO.getPrice());
        existing.setStock(productDTO.getStock());
        existing.setImageUrl(productDTO.getImageUrl());
        existing.setCategory(findCategoryById(productDTO.getCategory().getId()));
        return toDTO(productRepository.save(existing));
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Produit introuvable avec l'id " + id);
        }
        productRepository.deleteById(id);
    }

    private Product findEntityById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit introuvable avec l'id " + id));
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Catégorie introuvable avec l'id " + id));
    }

    private ProductDTO toDTO(Product product) {
        CategoryDTO categoryDTO = new CategoryDTO(
                product.getCategory().getId(),
                product.getCategory().getName(),
                product.getCategory().getDescription()
        );
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                categoryDTO,                    // ← repositionné en 4ème
                product.getPrice(),
                product.getStock(),
                product.getImageUrl(),
                product.getDateCreation()
        );
    }

    private Product toEntity(ProductDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setImageUrl(dto.getImageUrl());
        product.setCategory(findCategoryById(dto.getCategory().getId()));
        return product;
    }


}