package com.example.semana1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

    // Páginas Públicas
    @GetMapping({"/", "/home"})
    public String home() {
        return "home"; // Muestra home.html
    }

    @GetMapping("/registro")
    public String registro() {
        return "registro"; // Muestra registro.html
    }
    
    @GetMapping("/busqueda")
    public String busqueda() {
        return "busqueda"; // Muestra busqueda.html
    }
    
    // Mapeo para la página de login (la usamos personalizada)
    @GetMapping("/login")
    public String login() {
        return "login"; // Muestra login.html
    }
    
    // Páginas Privadas (Acceso bloqueado por SecurityConfig si no hay login)
    @GetMapping("/perfil")
    public String perfil() {
        return "perfil"; // Muestra perfil.html
    }

    @GetMapping("/arriendo")
    public String arriendo() {
        return "arriendo"; // Muestra arriendo.html
    }
}