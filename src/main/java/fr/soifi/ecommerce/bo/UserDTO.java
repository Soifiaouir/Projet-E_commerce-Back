package fr.soifi.ecommerce.bo;

import fr.soifi.ecommerce.dal.entity.Address;
import fr.soifi.ecommerce.dal.entity.Role;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO {
    private Long id;
    private String email;
    private Role role;
    private Address address;
    private LocalDateTime dateCreation;
}
