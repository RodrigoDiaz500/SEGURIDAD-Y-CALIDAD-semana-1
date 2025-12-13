package com.example.semana1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth
                    // Público
                    .requestMatchers("/api/maquinaria/publica").permitAll()

                    // Protegido
                    .requestMatchers("/api/maquinaria/**").authenticated()

                    // Todo lo demás (por si agregas otros endpoints)
                    .anyRequest().permitAll()
            )

            // Para permitir @WithMockUser en tests y evitar redirects
            .httpBasic(httpBasic -> {})

            // Opcional pero recomendado para APIs
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
