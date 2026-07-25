package fr.soifi.ecommerce.bo;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private CategoryDTO category;
    private BigDecimal price;
    private Integer stock;
    private String imageUrl;
    private LocalDateTime dateCreation;
}
