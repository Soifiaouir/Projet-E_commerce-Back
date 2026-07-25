package fr.soifi.ecommerce.bo;

import fr.soifi.ecommerce.dal.entity.User;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CartDTO {
    private Long id;
    private User user;
    private List<CartItemDTO> items;
}

