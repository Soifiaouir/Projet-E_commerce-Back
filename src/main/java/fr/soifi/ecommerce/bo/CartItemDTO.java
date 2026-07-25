package fr.soifi.ecommerce.bo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CartItemDTO {
    private Long id;
    private ProductDTO product;
    private Integer quantity;
}
