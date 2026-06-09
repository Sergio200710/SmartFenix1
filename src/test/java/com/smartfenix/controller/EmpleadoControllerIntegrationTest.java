package com.smartfenix.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartfenix.domain.Empleado;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EmpleadoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testListarEmpleadosDevuelveOk() throws Exception {
        mockMvc.perform(get("/api/empleados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(0)));
    }

    @Test
    public void testCrearEmpleadoFlujoCompleto() throws Exception {
        Empleado nuevoEmpleado = Empleado.builder()
                .nombre("Carlos Gomez")
                .email("carlos.gomez@smartfenix.com")
                .rol("Desarrollador Frontend")
                .build();

        mockMvc.perform(post("/api/empleados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoEmpleado)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre", is("Carlos Gomez")))
                .andExpect(jsonPath("$.email", is("carlos.gomez@smartfenix.com")))
                .andExpect(jsonPath("$.rol", is("Desarrollador Frontend")));
    }

    @Test
    public void testConsultarEmpleadoInexistenteDevuelveNotFound() throws Exception {
        mockMvc.perform(get("/api/empleados/9999"))
                .andExpect(status().isNotFound());
    }
}
