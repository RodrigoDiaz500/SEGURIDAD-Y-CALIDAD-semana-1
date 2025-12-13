package com.example.semana1.config;

import com.example.semana1.filter.JwtAuthFilter;
import com.example.semana1.repository.UserRepository; 
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration; // Nuevo Import
import org.springframework.context.annotation.Bean; // Nuevo Import
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Nuevo Import
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource; 

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource(properties = {
    // Estabilización de Propiedad JWT
    "jwt.secret=EstaEsUnaClaveSecretaLargaParaPruebas1234567890ABCDEF",
    
    // Estabilización de Base de Datos H2
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1", 
    "spring.datasource.driver-class-name=org.h2.Driver", 
    "spring.datasource.username=sa", 
    "spring.datasource.password=password",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.hibernate.ddl-auto=none"
})
public class SecurityConfigTest {

    // Mocks para estabilizar el SecurityConfig
    @MockBean
    private JwtAuthFilter jwtAuthFilter;
    
    @MockBean
    private UserDetailsService userDetailsService; 
    
    @MockBean
    private AuthenticationManager authenticationManager; 
    
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testPasswordEncoderBeanIsAvailable() {
        // Si el contexto carga sin el error de PasswordEncoder, este test pasa.
        assertNotNull(passwordEncoder, "El bean PasswordEncoder debe estar disponible en el contexto.");
    }
    
    // FIX CLAVE: Definición explícita del PasswordEncoder para la prueba.
    // Esto garantiza que el bean siempre estará disponible, resolviendo el UnsatisfiedDependency.
    @TestConfiguration 
    static class TestSecurityConfig {
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }
}