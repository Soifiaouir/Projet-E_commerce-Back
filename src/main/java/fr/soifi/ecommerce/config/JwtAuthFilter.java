package fr.soifi.ecommerce.config;

import fr.soifi.ecommerce.dal.entity.User;
import fr.soifi.ecommerce.dal.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
//classe de base de Spring qui garantit que ce filtre s'exécute une seule fois par requête HTTP entrante
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public JwtAuthFilter(JwtUtils jwtUtils, UserRepository userRepository) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        //le front Angular enverra ses requêtes avec un header du type Authorization: Bearer
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7); // enleve "Bearer "

        //vérifie que le token n'est pas expiré et que sa signature est valide
        if (jwtUtils.validateToken(token)) {
            String email = jwtUtils.getEmailFromToken(token);

            User user = userRepository.findByEmail(email).orElse(null);

            if (user != null) {
                var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

                var authentication = new UsernamePasswordAuthenticationToken(
                        user.getEmail(), null, authorities
                );

                //dit à Spring Security "pour le reste du traitement de cette requête, considère que cet utilisateur est authentifié, avec ce rôle"
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}