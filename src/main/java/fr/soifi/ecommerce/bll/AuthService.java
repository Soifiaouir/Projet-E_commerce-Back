package fr.soifi.ecommerce.bll;

import fr.soifi.ecommerce.bo.JwtResponse;
import fr.soifi.ecommerce.bo.LoginRequest;
import fr.soifi.ecommerce.bo.RegisterRequest;

public interface AuthService {
    JwtResponse register(RegisterRequest request);
    JwtResponse login(LoginRequest request);
}