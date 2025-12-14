package com.example.semana1.controller;

import com.example.semana1.model.AuthRequest;
import com.example.semana1.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

    @PostMapping(
    value = "/login",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.TEXT_PLAIN_VALUE
)
public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {

    try {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(),
                authRequest.getPassword()
            )
        );

        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        }

        return "";

    } catch (Exception ex) {
        
        return "";
    }
}
}
