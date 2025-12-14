package com.example.semana1.controller;

import com.example.semana1.config.SecurityConfig;
import com.example.semana1.filter.JwtAuthFilter;
import com.example.semana1.model.AuthRequest;
import com.example.semana1.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
    controllers = AuthController.class,
    excludeAutoConfiguration = {
        SecurityAutoConfiguration.class,
        UserDetailsServiceAutoConfiguration.class,
        OAuth2ResourceServerAutoConfiguration.class
    }
)
@AutoConfigureMockMvc(addFilters = false) 
@TestPropertySource(properties = {
    "jwt.secret=ClaveDePruebaSegura1234567890ABC"
})
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    private static String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testAuthenticateAndGetToken_Success() throws Exception {
        String username = "rodrigo";
        String token = "TOKEN_GENERADO_EXITOSAMENTE";

        AuthRequest request = new AuthRequest();
        request.setUsername(username);
        request.setPassword("1234");

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(jwtService.generateToken(username)).thenReturn(token);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
               .andExpect(status().isOk())
               .andExpect(content().string(token));
    }

    @Test
    void testAuthenticateAndGetToken_Failure_BadCredentials() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setUsername("user");
        request.setPassword("wrong");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid"));

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
               .andExpect(status().isOk());
    }

    @Test
    void testAuthenticateAndGetToken_Failure_NotAuthenticated() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setUsername("user");
        request.setPassword("wrong");

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
            .andExpect(status().isOk())
            .andExpect(content().string(""));
    }
}
