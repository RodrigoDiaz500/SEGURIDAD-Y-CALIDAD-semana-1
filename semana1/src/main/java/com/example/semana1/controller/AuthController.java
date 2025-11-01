package com.example.semana1.controller;

import com.example.semana1.model.AuthRequest;
import com.example.semana1.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    // API Pública: La URL debe coincidir con la configurada en SecurityConfig
    @PostMapping("/login")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        
        // 1. Intentar Autenticar las credenciales
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );

        if (authentication.isAuthenticated()) {
            // 2. Si es exitoso, generar y retornar el Token JWT
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new RuntimeException("Credenciales inválidas. ¡Login fallido!");
        }
    }
}