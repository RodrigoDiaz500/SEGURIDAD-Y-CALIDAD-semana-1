package com.example.semana1.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ModelTests {

    @Test
    void testUserEntity() {
        User user = new User();
        
        // Probar Setters
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("hashedpass");
        user.setRole("ADMIN");

        // Probar Getters
        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("hashedpass", user.getPassword());
        assertEquals("ADMIN", user.getRole());
    }

    @Test
    void testAuthRequestModel() {
        AuthRequest request = new AuthRequest();
        
        // Probar Setters
        request.setUsername("loginuser");
        request.setPassword("loginpass");

        // Probar Getters
        assertEquals("loginuser", request.getUsername());
        assertEquals("loginpass", request.getPassword());
    }
}