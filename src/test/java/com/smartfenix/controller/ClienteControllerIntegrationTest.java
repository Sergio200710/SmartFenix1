package com.smartfenix.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartfenix.domain.Cliente;
import com.smartfenix.domain.Proyecto;
import com.smartfenix.repository.ClienteRepository;
import com.smartfenix.repository.ProyectoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ClienteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Test
    public void testListarClientesDevuelveOk() throws Exception {
        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(0)));
    }

    @Test
    public void testCrearClienteDevuelveCreated() throws Exception {
        Cliente cliente = Cliente.builder()
                .nombre("Cliente Test")
                .empresa("Empresa Test")
                .telefono("600123123")
                .build();

        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre", is("Cliente Test")))
                .andExpect(jsonPath("$.empresa", is("Empresa Test")))
                .andExpect(jsonPath("$.telefono", is("600123123")));
    }

    @Test
    public void testConsultarClienteInexistenteDevuelveNotFound() throws Exception {
        mockMvc.perform(get("/api/clientes/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testEliminarClienteConProyectosDevuelveConflict() throws Exception {
        Cliente cliente = clienteRepository.save(Cliente.builder()
                .nombre("Cliente Conflicto")
                .empresa("Empresa Conflicto")
                .telefono("611111111")
                .build());

        proyectoRepository.save(Proyecto.builder()
                .nombre("Proyecto Asociado Cliente")
                .cliente(cliente)
                .build());

        mockMvc.perform(delete("/api/clientes/{id}", cliente.getId()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$", hasKey("error")))
                .andExpect(jsonPath("$.error", containsString("proyectos asociados")));
    }
}
