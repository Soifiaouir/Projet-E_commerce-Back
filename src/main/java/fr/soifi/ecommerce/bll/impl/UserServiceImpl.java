package fr.soifi.ecommerce.bll.impl;

import fr.soifi.ecommerce.bll.UserService;
import fr.soifi.ecommerce.bo.UserDTO;
import fr.soifi.ecommerce.dal.entity.User;
import fr.soifi.ecommerce.dal.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO getUserById(Long id) {
        return toDTO(findEntityById(id));
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'email " + email));
        return toDTO(user);
    }

    private User findEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'id " + id));
    }

    private UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setAddress(user.getAddress());
        dto.setDateCreation(user.getDateCreation());
        // pas de password : jamais renvoyé au front, même hashé
        return dto;
    }
}