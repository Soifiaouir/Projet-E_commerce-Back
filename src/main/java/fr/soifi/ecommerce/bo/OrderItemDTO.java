package fr.soifi.ecommerce.bo;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderItemDTO {
    private Long id;
    private ProductDTO product;
    private Integer quantity;
    private BigDecimal prixUnitaire;
}
