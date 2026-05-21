package com.smartfenix.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartfenix.domain.Empleado;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
public class EmpleadoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // CASO DE PRUEBA: Alta de empleado y consulta por ID - flujo completo de integración desde Controller hasta MySQL
    @Test
    public void testCrearEmpleadoFlujoCompleto() throws Exception {
        Empleado nuevoEmpleado = Empleado.builder()
                .nombre("Carlos Gomez")
                .email("carlos.gomez@smartfenix.com")
                .rol("Desarrollador Frontend")
                .build();

        String responseContent = mockMvc.perform(post("/api/empleados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoEmpleado)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre", is("Carlos Gomez")))
                .andExpect(jsonPath("$.email", is("carlos.gomez@smartfenix.com")))
                .andExpect(jsonPath("$.rol", is("Desarrollador Frontend")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Empleado creado = objectMapper.readValue(responseContent, Empleado.class);
        Long idGenerado = creado.getId();

        mockMvc.perform(get("/api/empleados/" + idGenerado))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(idGenerado.intValue())))
                .andExpect(jsonPath("$.nombre", is("Carlos Gomez")))
                .andExpect(jsonPath("$.email", is("carlos.gomez@smartfenix.com")));
    }

    // CASO DE PRUEBA: Validación de formato de email - comprobar que enviar un email inválido responde HTTP 400 Bad Request
    @Test
    public void testCrearEmpleadoConEmailInvalidoDevuelveBadRequest() throws Exception {
        Empleado nuevoEmpleado = Empleado.builder()
                .nombre("Pedro Perez")
                .email("email-invalido")
                .rol("Tester")
                .build();

        mockMvc.perform(post("/api/empleados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoEmpleado)))
                .andExpect(status().isBadRequest());
    }

    // CASO DE PRUEBA: Validación de nombre obligatorio - comprobar que enviar un nombre vacío responde HTTP 400 Bad Request
    @Test
    public void testCrearEmpleadoConNombreVacioDevuelveBadRequest() throws Exception {
        Empleado nuevoEmpleado = Empleado.builder()
                .nombre("")
                .email("pedro.perez@smartfenix.com")
                .rol("Tester")
                .build();

        mockMvc.perform(post("/api/empleados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoEmpleado)))
                .andExpect(status().isBadRequest());
    }
}
