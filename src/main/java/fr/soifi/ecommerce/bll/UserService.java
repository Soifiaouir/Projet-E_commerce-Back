package fr.soifi.ecommerce.bll;

import fr.soifi.ecommerce.bo.UserDTO;

public interface UserService {
    UserDTO getUserById(Long id);
    UserDTO getUserByEmail(String email);
}