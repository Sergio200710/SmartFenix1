package com.smartfenix.service;

import com.smartfenix.domain.Cliente;
import com.smartfenix.domain.Proyecto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProyectoServiceTest {

    @Autowired
    private ProyectoService proyectoService;

    @Autowired
    private ClienteService clienteService;

    // CASO DE PRUEBA: Alta de proyecto - comprobar que el proyecto se asocia a un cliente y se guarda correctamente
    @Test
    public void testCrearProyecto() {
        Cliente cliente = Cliente.builder()
                .nombre("Amazon")
                .empresa("Amazon Web Services")
                .telefono("123456789")
                .build();
        Cliente clienteGuardado = clienteService.save(cliente);

        Proyecto proyecto = Proyecto.builder()
                .nombre("Proyecto Migración AWS")
                .fechaInicio(LocalDate.now())
                .fechaFin(LocalDate.now().plusDays(45))
                .cliente(clienteGuardado)
                .build();
        Proyecto guardado = proyectoService.save(proyecto);

        assertNotNull(guardado.getId());
        assertEquals("Proyecto Migración AWS", guardado.getNombre());
        assertEquals(clienteGuardado.getId(), guardado.getCliente().getId());
    }

    // CASO DE PRUEBA: Búsqueda de proyecto por ID - comprobar que un proyecto y su cliente asociado se recuperan correctamente
    @Test
    public void testBuscarProyecto() {
        Cliente cliente = Cliente.builder()
                .nombre("Sony")
                .empresa("Sony Europe")
                .telefono("987654321")
                .build();
        Cliente clienteGuardado = clienteService.save(cliente);

        Proyecto proyecto = Proyecto.builder()
                .nombre("Proyecto Playstation")
                .fechaInicio(LocalDate.now())
                .fechaFin(LocalDate.now().plusDays(60))
                .cliente(clienteGuardado)
                .build();
        Proyecto guardado = proyectoService.save(proyecto);

        Optional<Proyecto> encontrado = proyectoService.findById(guardado.getId());

        assertTrue(encontrado.isPresent());
        assertEquals("Proyecto Playstation", encontrado.get().getNombre());
        assertNotNull(encontrado.get().getCliente());
    }

    // CASO DE PRUEBA: Actualización de proyecto - comprobar que los campos modificados se actualizan correctamente
    @Test
    public void testActualizarProyecto() {
        Cliente cliente = Cliente.builder()
                .nombre("Google")
                .empresa("Google LLC")
                .telefono("111222333")
                .build();
        Cliente clienteGuardado = clienteService.save(cliente);

        Proyecto proyecto = Proyecto.builder()
                .nombre("Proyecto Inicial")
                .fechaInicio(LocalDate.now())
                .fechaFin(LocalDate.now().plusDays(30))
                .cliente(clienteGuardado)
                .build();
        Proyecto proyectoGuardado = proyectoService.save(proyecto);

        proyectoGuardado.setNombre("Proyecto Actualizado");

        Optional<Proyecto> proyectoActualizadoOpt = proyectoService.update(proyectoGuardado.getId(), proyectoGuardado);

        assertTrue(proyectoActualizadoOpt.isPresent());
        Proyecto proyectoActualizado = proyectoActualizadoOpt.get();
        assertEquals("Proyecto Actualizado", proyectoActualizado.getNombre());
        assertEquals(clienteGuardado.getId(), proyectoActualizado.getCliente().getId());
    }
}
