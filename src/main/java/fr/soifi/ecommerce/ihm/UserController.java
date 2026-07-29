package fr.soifi.ecommerce.ihm;

import fr.soifi.ecommerce.bll.UserService;
import fr.soifi.ecommerce.bo.UserDTO;
import fr.soifi.ecommerce.config.CurrentUserProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final CurrentUserProvider currentUserProvider;

    public UserController(UserService userService, CurrentUserProvider currentUserProvider) {
        this.userService = userService;
        this.currentUserProvider = currentUserProvider;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser() {
        Long userId = currentUserProvider.getCurrentUserId();
        return ResponseEntity.ok(userService.getUserById(userId));
    }
}