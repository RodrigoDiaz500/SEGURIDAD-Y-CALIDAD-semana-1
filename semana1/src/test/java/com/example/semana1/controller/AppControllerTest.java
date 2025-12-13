package com.example.semana1.controller;

import com.example.semana1.filter.JwtAuthFilter;
import com.example.semana1.service.JwtService;
import com.example.semana1.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Solución 1: Desactivar filtros de seguridad para probar las vistas públicas
@WebMvcTest(controllers = AppController.class,
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthFilter.class))
@AutoConfigureMockMvc(addFilters = false) 
public class AppControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Se mantienen los MockBeans para estabilizar el contexto
    @MockBean
    private JwtService jwtService;
    
    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    

    @Test
    void testHomeEndpoint() throws Exception {
        mockMvc.perform(get("/home"))
               .andExpect(status().isOk())
               .andExpect(view().name("home")); 
    }
    
    @Test
    void testRegistroEndpoint() throws Exception {
        mockMvc.perform(get("/registro"))
               .andExpect(status().isOk())
               .andExpect(view().name("registro"));
    }
    
    @Test
    void testBusquedaEndpoint() throws Exception {
        mockMvc.perform(get("/busqueda"))
               .andExpect(status().isOk())
               .andExpect(view().name("busqueda"));
    }
    
    @Test
    void testLoginEndpoint() throws Exception {
        mockMvc.perform(get("/login"))
               .andExpect(status().isOk())
               .andExpect(view().name("login"));
    }
}