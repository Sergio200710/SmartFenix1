package com.smartfenix.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartfenix.domain.Empleado;
import com.smartfenix.domain.Proyecto;
import com.smartfenix.domain.Tarea;
import com.smartfenix.repository.EmpleadoRepository;
import com.smartfenix.repository.ProyectoRepository;
import com.smartfenix.repository.TareaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasKey;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private TareaRepository tareaRepository;

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

    @Test
    public void testEliminarEmpleadoConTareasDevuelveConflict() throws Exception {
        Empleado empleado = empleadoRepository.save(Empleado.builder()
                .nombre("Empleado Conflicto")
                .email("empleado.conflicto@smartfenix.com")
                .rol("Backend")
                .build());

        Proyecto proyecto = proyectoRepository.save(Proyecto.builder()
                .nombre("Proyecto Conflicto Empleado")
                .build());

        tareaRepository.save(Tarea.builder()
                .descripcion("Tarea asociada")
                .estado("Pendiente")
                .empleado(empleado)
                .proyecto(proyecto)
                .build());

        mockMvc.perform(delete("/api/empleados/{id}", empleado.getId()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$", hasKey("error")))
                .andExpect(jsonPath("$.error", containsString("tareas asignadas")));
    }
}
