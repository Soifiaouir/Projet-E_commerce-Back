package fr.soifi.ecommerce.bo;

import fr.soifi.ecommerce.dal.entity.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JwtResponse {
    private String token;
    private String email;
    private Role role;
}
