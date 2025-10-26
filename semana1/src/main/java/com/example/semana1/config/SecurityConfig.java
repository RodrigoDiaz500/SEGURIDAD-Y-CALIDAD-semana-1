package com.example.semana1.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // --- 1. Definir Usuarios en Memoria (Requisito de 3 usuarios) ---
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        // {noop} indica que la contraseña no está encriptada (solo para desarrollo)
        
        // Usuario 1: Agricultor
        UserDetails user1 = User.withUsername("agricultor1")
            .password("{noop}pass123")
            .roles("USER")
            .build();
        
        // Usuario 2: Dueño de Maquinaria
        UserDetails user2 = User.withUsername("dueno1")
            .password("{noop}pass456")
            .roles("USER")
            .build();
            
        // Usuario 3: Administrador (Ejemplo)
        UserDetails admin = User.withUsername("admin")
            .password("{noop}admin789")
            .roles("ADMIN", "USER")
            .build();
            
        return new InMemoryUserDetailsManager(user1, user2, admin);
    }
    
    // --- 2. Configurar Reglas de Acceso (Público vs. Privado) ---
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                // PÁGINAS PÚBLICAS: Accesibles por cualquiera
                .requestMatchers("/", "/home", "/registro", "/busqueda", "/css/**").permitAll()
                // PÁGINAS PRIVADAS: Solo accesibles si estás autenticado
                .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                .loginPage("/login") // Usa nuestra página de login personalizada
                .permitAll()
            )
            .logout((logout) -> logout.permitAll());

        return http.build();
    }
}