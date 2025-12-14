package com.example.semana1.controller;

import com.example.semana1.config.SecurityConfig;
import com.example.semana1.filter.JwtAuthFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
    controllers = MaquinariaController.class,
    excludeAutoConfiguration = {
        SecurityAutoConfiguration.class,
        UserDetailsServiceAutoConfiguration.class,
        OAuth2ResourceServerAutoConfiguration.class
    }
)
@AutoConfigureMockMvc(addFilters = false) 
public class MaquinariaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @MockBean
    private UserDetailsService userDetailsService;

    private final String STATIC_JSON_RESPONSE = "[\n" +
            "  {\"id\": 101, \"nombre\": \"Tractor JD 6000\", \"tipo\": \"Tractor\", \"ubicacion\": \"Santiago\", \"precio\": 50000, \"descripcion\": \"Detalles básicos, visibles públicamente, para la búsqueda.\"},\n" +
            "  {\"id\": 102, \"nombre\": \"Cosechadora CX\", \"tipo\": \"Cosechadora\", \"ubicacion\": \"Rancagua\", \"precio\": 80000, \"descripcion\": \"Detalles básicos, visibles públicamente, para la búsqueda.\"}\n" +
            "]";

    private final String PRIVATE_JSON_FORMAT = "{\n" +
            "  \"id\": %d,\n" +
            "  \"nombre\": \"Tractor JD 6000\",\n" +
            "  \"marca\": \"John Deere\",\n" +
            "  \"anio\": 2018,\n" +
            "  \"mantenciones\": \"Última hace 3 meses.\",\n" +
            "  \"condiciones_arriendo\": \"Seguro obligatorio, mínimo 3 días.\",\n" +
            "  \"fotografias\": \"[url-foto-1, url-foto-2]\"\n" +
            "}";

    @Test
    void testGetPublicMaquinaria_NoParams() throws Exception {
        mockMvc.perform(get("/api/maquinaria/publica"))
               .andExpect(status().isOk())
               .andExpect(content().string(STATIC_JSON_RESPONSE));
    }

    @Test
    void testGetPublicMaquinaria_WithTipoParam() throws Exception {
        String tipo = "Tractor";

        String expected = "Simulando búsqueda de maquinaria tipo: " + tipo +
                ". Resultados estáticos: " + STATIC_JSON_RESPONSE;

        mockMvc.perform(get("/api/maquinaria/publica")
                .param("tipo", tipo))
               .andExpect(status().isOk())
               .andExpect(content().string(expected));
    }

    @Test
    @WithMockUser
    void testGetPrivateMaquinariaDetail_Authenticated() throws Exception {
        Long id = 101L;
        String expected = String.format(PRIVATE_JSON_FORMAT, id);

        mockMvc.perform(get("/api/maquinaria/" + id))
               .andExpect(status().isOk())
               .andExpect(content().string(expected));
    }

    @Test
    void testGetPrivateMaquinariaDetail_Unauthenticated() throws Exception {
        mockMvc.perform(get("/api/maquinaria/101"))
               .andExpect(status().isOk());
    }
}
