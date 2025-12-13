package com.example.semana1.service;

import com.example.semana1.model.User;
import com.example.semana1.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Usa MockitoExtension para inicializar los mocks y las inyecciones
@ExtendWith(MockitoExtension.class) 
public class UserDetailsServiceImplTest {

    // FIX: Usa @Mock para crear el mock del repositorio
    @Mock 
    private UserRepository repository;

    // FIX: Usa @InjectMocks para inyectar el mock en la instancia del servicio que se va a probar
    @InjectMocks
    private UserDetailsServiceImpl service; 

    // **Nota:** Debes tener un constructor o setters en tu clase User para que esto funcione.
    // Creamos un mock User para simular el objeto devuelto por la BD
    private User createMockUser(String username, String password) {
        User user = new User();
        // Asumiendo que tienes métodos setUsername/setPassword
        user.setUsername(username);
        user.setPassword(password);
        user.setRole("USER"); 
        return user;
    }

    @Test
    void testLoadUserByUsername_Success() {
        String username = "testuser";
        User mockUser = createMockUser(username, "encodedpassword");
        
        when(repository.findByUsername(username)).thenReturn(Optional.of(mockUser));

        UserDetails userDetails = service.loadUserByUsername(username);

        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        verify(repository, times(1)).findByUsername(username);
    }

    // FIX: Cubre la rama de excepción (UsernameNotFoundException) para alcanzar el 100%
    @Test
    void testLoadUserByUsername_UserNotFound() {
        String nonExistentUser = "usuario_inexistente";
        
        when(repository.findByUsername(nonExistentUser)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            service.loadUserByUsername(nonExistentUser);
        });
        
        verify(repository, times(1)).findByUsername(nonExistentUser);
    }
}