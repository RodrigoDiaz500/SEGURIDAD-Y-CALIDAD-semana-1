package com.example.semana1.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {

    private JwtService jwtService;
    private final String TEST_SECRET = "EstaEsUnaClaveSecretaLargaParaPruebas1234567890ABCDEF"; 
    private final String TEST_USERNAME = "testuser";
    private UserDetails userDetails;

    @BeforeEach
    public void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "SECRET", TEST_SECRET);

        userDetails = new User(TEST_USERNAME, "password", Collections.emptyList());
    }
    
    // --- Métodos de Generación/Extracción ---

    @Test
    void testGenerateTokenAndExtractUsername() {
        String token = jwtService.generateToken(TEST_USERNAME);
        assertNotNull(token);
        assertEquals(TEST_USERNAME, jwtService.extractUsername(token));
    }

    @Test
    void testExtractExpiration() {
        String token = jwtService.generateToken(TEST_USERNAME);
        Date expiration = jwtService.extractExpiration(token);
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }
    
    // --- Métodos de Validación ---

    @Test
    void testValidateToken_Success() {
        String token = jwtService.generateToken(TEST_USERNAME);
        assertTrue(jwtService.validateToken(token, userDetails)); 
    }

    @Test
    void testValidateToken_Failure_DifferentUser() {
        String token = jwtService.generateToken("otheruser");
        assertFalse(jwtService.validateToken(token, userDetails)); 
    }


    @Test
    void testValidateToken_Failure_Expired() {
        // Generar un token que expire hace 1 hora para evitar el error de tiempo de ejecución .
        long oneHourAgo = System.currentTimeMillis() - 3600000; 
        
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(TEST_SECRET));
        
        String expiredToken = Jwts.builder()
                .setSubject(TEST_USERNAME)
                .setIssuedAt(new Date(oneHourAgo - 60000))
                .setExpiration(new Date(oneHourAgo)) 
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        
        assertThrows(io.jsonwebtoken.ExpiredJwtException.class, () -> {
            jwtService.validateToken(expiredToken, userDetails);
        });
    }
}