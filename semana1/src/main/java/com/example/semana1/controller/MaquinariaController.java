package com.example.semana1.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/maquinaria") // Cambiado de /api/recetas
public class MaquinariaController {

    /**
     * [Pública] API para búsqueda y listado general de maquinaria.
     * URL: /api/maquinaria/publica
     * No requiere JWT.
     */
    @GetMapping("/publica")
    public String getPublicMaquinaria(@RequestParam(required = false) String tipo,
                                    @RequestParam(required = false) String ubicacion) {
        // En esta semana, la data es estática y simula la respuesta de la BD
        String response = "[\n" +
                "  {\"id\": 101, \"nombre\": \"Tractor JD 6000\", \"tipo\": \"Tractor\", \"ubicacion\": \"Santiago\", \"precio\": 50000, \"descripcion\": \"Detalles básicos, visibles públicamente, para la búsqueda.\"},\n" +
                "  {\"id\": 102, \"nombre\": \"Cosechadora CX\", \"tipo\": \"Cosechadora\", \"ubicacion\": \"Rancagua\", \"precio\": 80000, \"descripcion\": \"Detalles básicos, visibles públicamente, para la búsqueda.\"}\n" +
                "]";
        
        // La lógica real buscaría en la BD por tipo, ubicación, etc.
        if (tipo != null) {
            return "Simulando búsqueda de maquinaria tipo: " + tipo + ". Resultados estáticos: " + response;
        }

        return response;
    }

    /**
     * [Privada] API para visualizar los detalles completos de la maquinaria.
     * URL: /api/maquinaria/{id}
     * Requiere JWT válido.
     */
    @GetMapping("/{id}")
    public String getPrivateMaquinariaDetail(@PathVariable Long id) {
        // Solo se llega aquí si el Token JWT es válido.
        // El requisito es que el detalle es código estático.
        return "{\n" +
                "  \"id\": " + id + ",\n" +
                "  \"nombre\": \"Tractor JD 6000\",\n" +
                "  \"marca\": \"John Deere\",\n" +
                "  \"anio\": 2018,\n" +
                "  \"mantenciones\": \"Última hace 3 meses.\",\n" +
                "  \"condiciones_arriendo\": \"Seguro obligatorio, mínimo 3 días.\",\n" +
                "  \"fotografias\": \"[url-foto-1, url-foto-2]\"\n" +
                "}";
    }
}