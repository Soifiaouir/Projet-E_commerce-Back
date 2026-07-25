package fr.soifi.ecommerce.bo;

import fr.soifi.ecommerce.dal.entity.Address;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegisterRequest {
    private String email;
    private String password;
    private Address address;
}
