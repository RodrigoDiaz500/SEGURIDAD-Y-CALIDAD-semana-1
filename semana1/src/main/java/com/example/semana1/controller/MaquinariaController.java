package com.example.semana1.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/maquinaria")
public class MaquinariaController {

    @GetMapping(
        value = "/publica",
        produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String getPublicMaquinaria(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String ubicacion) {

        String response = "[\n" +
                "  {\"id\": 101, \"nombre\": \"Tractor JD 6000\", \"tipo\": \"Tractor\", \"ubicacion\": \"Santiago\", \"precio\": 50000, \"descripcion\": \"Detalles básicos, visibles públicamente, para la búsqueda.\"},\n" +
                "  {\"id\": 102, \"nombre\": \"Cosechadora CX\", \"tipo\": \"Cosechadora\", \"ubicacion\": \"Rancagua\", \"precio\": 80000, \"descripcion\": \"Detalles básicos, visibles públicamente, para la búsqueda.\"}\n" +
                "]";

        if (tipo != null) {
            return "Simulando búsqueda de maquinaria tipo: " + tipo + ". Resultados estáticos: " + response;
        }

        return response;
    }

    @GetMapping(
        value = "/{id}",
        produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String getPrivateMaquinariaDetail(@PathVariable Long id) {
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
