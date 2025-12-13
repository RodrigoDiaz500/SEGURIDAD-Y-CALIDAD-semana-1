package com.example.semana1.filter;

import com.example.semana1.service.JwtService;
import com.example.semana1.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtAuthFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    @Mock
    private HttpServletRequest request;
    
    @Mock
    private HttpServletResponse response;
    
    @Mock
    private FilterChain filterChain;
    
    private final String MOCK_TOKEN = "valid.jwt.token";
    private final String MOCK_USER = "filteruser";
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        // Limpiar el contexto de seguridad antes de cada prueba
        SecurityContextHolder.getContext().setAuthentication(null);
        userDetails = new User(MOCK_USER, "pass", Collections.emptyList());
    }

    @Test
    void testDoFilterInternal_ValidToken() throws ServletException, IOException {
        // 1. Simular la petición con un token válido
        when(request.getHeader("Authorization")).thenReturn("Bearer " + MOCK_TOKEN);
        when(jwtService.extractUsername(MOCK_TOKEN)).thenReturn(MOCK_USER);
        when(userDetailsService.loadUserByUsername(MOCK_USER)).thenReturn(userDetails);
        when(jwtService.validateToken(MOCK_TOKEN, userDetails)).thenReturn(true);
        
        // 2. Ejecutar el filtro
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // 3. Validación: El contexto de seguridad debe estar establecido y la cadena debe continuar
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }
    
    @Test
    void testDoFilterInternal_NoToken() throws ServletException, IOException {
        // Simular petición sin encabezado Authorization
        when(request.getHeader("Authorization")).thenReturn(null);

        // Ejecutar el filtro
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Validación: El contexto de seguridad debe ser nulo y el filtro debe continuar
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtService, never()).extractUsername(anyString()); // Verifica que la lógica de JWT no se ejecute
    }
    
    @Test
    void testDoFilterInternal_InvalidTokenPrefix() throws ServletException, IOException {
        // Simular petición con encabezado pero sin el prefijo "Bearer "
        when(request.getHeader("Authorization")).thenReturn("Basic invalid_token");

        // Ejecutar el filtro
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Validación: El contexto debe ser nulo
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_InvalidToken() throws ServletException, IOException {
        // Simular token inválido (nombre de usuario extraído pero la validación falla)
        when(request.getHeader("Authorization")).thenReturn("Bearer " + MOCK_TOKEN);
        when(jwtService.extractUsername(MOCK_TOKEN)).thenReturn(MOCK_USER);
        when(userDetailsService.loadUserByUsername(MOCK_USER)).thenReturn(userDetails);
        when(jwtService.validateToken(MOCK_TOKEN, userDetails)).thenReturn(false); // <--- Aquí falla la validación

        // Ejecutar el filtro
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Validación: El contexto de seguridad no debe estar establecido
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }
    @Test
void testDoFilterInternal_TokenPresentButUsernameNull() throws ServletException, IOException {
    // 1. Simular la petición con un token
    when(request.getHeader("Authorization")).thenReturn("Bearer " + MOCK_TOKEN);
    
    // 2. Simular que extractUsername devuelve null (token mal formado o expirado)
    when(jwtService.extractUsername(MOCK_TOKEN)).thenReturn(null);
    
    // 3. Ejecutar el filtro
    jwtAuthFilter.doFilterInternal(request, response, filterChain);

    // 4. Validación: El contexto debe ser nulo (ya que no se pudo autenticar) y el filtro debe continuar.
    assertNull(SecurityContextHolder.getContext().getAuthentication());
    verify(filterChain, times(1)).doFilter(request, response);
    // Verificamos que no se intentó buscar el usuario en la BD
    verify(userDetailsService, never()).loadUserByUsername(anyString()); 
}
@Test
    void testDoFilterInternal_NoHeader() throws Exception {
        // Petición SIN el encabezado Authorization
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();
        
        // Cubre la rama: if (authHeader == null || !authHeader.startsWith("Bearer "))
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Verifica que la cadena continúa y no hubo intento de autenticación
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        assertTrue(filterChain.getRequest() instanceof MockHttpServletRequest); 
    }
    
    @Test
    void testDoFilterInternal_AlreadyAuthenticated() throws Exception {
        // Cubre la rama: if (SecurityContextHolder.getContext().getAuthentication() != null)
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            "authenticatedUser", null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication); // Pre-autenticado

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/private");
        request.addHeader("Authorization", "Bearer token_valido");

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Verifica que la autenticación NO fue procesada de nuevo.
        assertEquals("authenticatedUser", SecurityContextHolder.getContext().getAuthentication().getName());
        SecurityContextHolder.clearContext(); 
    }
    
    @Test
    void testDoFilterInternal_InvalidFormatToken() throws Exception {
        // Cubre la rama: if (authHeader == null || !authHeader.startsWith("Bearer "))
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();
        
        request.addHeader("Authorization", "InvalidFormat token"); // Header presente pero sin "Bearer "

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Verifica que la cadena continúa y no hubo intento de autenticación
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

}