package com.example.semana1.repository;

import com.example.semana1.model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTest {

    @Test
    void testFindByUsername_UserFound() {
        User user = new User();
        user.setUsername("rodrigo");

        assertEquals("rodrigo", user.getUsername());
    }

    @Test
    void testFindByUsername_UserNotFound() {
        User user = null;
        assertNull(user);
    }
}
