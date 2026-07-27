package fr.soifi.ecommerce.bll.impl;

import fr.soifi.ecommerce.bll.AuthService;
import fr.soifi.ecommerce.bo.JwtResponse;
import fr.soifi.ecommerce.bo.LoginRequest;
import fr.soifi.ecommerce.bo.RegisterRequest;
import fr.soifi.ecommerce.config.JwtUtils;
import fr.soifi.ecommerce.dal.entity.Role;
import fr.soifi.ecommerce.dal.entity.User;
import fr.soifi.ecommerce.dal.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public JwtResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Un compte existe déjà avec cet email");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // ← hash ici
        user.setRole(Role.CLIENT);
        user.setAddress(request.getAddress());
        user.setDateCreation(LocalDateTime.now());

        userRepository.save(user);

        String token = jwtUtils.generateToken(user.getEmail());
        return new JwtResponse(token, user.getEmail(), user.getRole());
    }

    @Override
    public JwtResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email ou mot de passe incorrect"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Email ou mot de passe incorrect");
        }

        String token = jwtUtils.generateToken(user.getEmail());
        return new JwtResponse(token, user.getEmail(), user.getRole());
    }
}