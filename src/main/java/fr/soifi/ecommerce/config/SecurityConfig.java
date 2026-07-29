package fr.soifi.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // API stateless consommee par Angular : pas besoin de protection CSRF (pas de cookies/sessions)
                .csrf(csrf -> csrf.disable())

                // Aucune session cote serveur : chaque requete s'authentifie via son token JWT
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        // Authentification : accessible sans etre deja connecte
                        .requestMatchers("/api/auth/**").permitAll()

                        // Documentation API : accessible librement
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/api-docs/**").permitAll()

                        // Catalogue public : consultation libre du catalogue, meme sans compte
                        .requestMatchers(HttpMethod.GET, "/api/products/**", "/api/categories/**").permitAll()

                        // Gestion du catalogue : reservee aux administrateurs
                        .requestMatchers(HttpMethod.POST, "/api/products/**", "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/products/**", "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/products/**", "/api/categories/**").hasRole("ADMIN")

                        // Changement de statut d'une commande : reserve aux administrateurs
                        .requestMatchers(HttpMethod.PUT, "/api/orders/*/status").hasRole("ADMIN")

                        // Tout le reste (panier, commandes, profil...) : necessite d'etre authentifie
                        .anyRequest().authenticated()
                )

                // Insere le filtre JWT avant le filtre d'authentification standard de Spring
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}