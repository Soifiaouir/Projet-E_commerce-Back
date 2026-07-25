package fr.soifi.ecommerce.bo;

import fr.soifi.ecommerce.dal.entity.Address;
import fr.soifi.ecommerce.dal.entity.OrderItem;
import fr.soifi.ecommerce.dal.entity.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderDTO {
    private Long id;
    private UserDTO user;
    private List<OrderItemDTO> items;
    private OrderStatus status;
    private LocalDateTime dateCommande;
    private Address shippingAddress;
    private BigDecimal montantTotal;

}
