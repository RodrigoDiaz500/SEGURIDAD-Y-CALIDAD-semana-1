package com.example.semana1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

    // Páginas Públicas
    @GetMapping({"/", "/home"})
    public String home() {
        return "home"; 
    }

    @GetMapping("/registro")
    public String registro() {
        return "registro"; 
    }
    
    @GetMapping("/busqueda")
    public String busqueda() {
        return "busqueda"; 
    }
    
    // Mapeo para la página de login 
    @GetMapping("/login")
    public String login() {
        return "login"; 
    }
    
    // Páginas Privadas (Acceso bloqueado por SecurityConfig si no hay login)
    @GetMapping("/perfil")
    public String perfil() {
        return "perfil"; 
    }

    @GetMapping("/arriendo")
    public String arriendo() {
        return "arriendo";
    }
}