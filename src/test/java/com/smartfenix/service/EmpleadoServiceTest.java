package com.smartfenix.service;

import com.smartfenix.domain.Empleado;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EmpleadoServiceTest {

    @Autowired
    private EmpleadoService empleadoService;

    // CASO DE PRUEBA: Alta de empleado - comprobar que el empleado se guarda correctamente en la base de datos
    @Test
    public void testCrearEmpleado() {
        Empleado empleado = Empleado.builder()
                .nombre("Laura Montes")
                .email("laura.montes@smartfenix.com")
                .rol("Diseñadora UI/UX")
                .build();

        Empleado guardado = empleadoService.save(empleado);

        assertNotNull(guardado.getId());
        assertEquals("Laura Montes", guardado.getNombre());
        assertEquals("laura.montes@smartfenix.com", guardado.getEmail());
        assertEquals("Diseñadora UI/UX", guardado.getRol());
    }

    // CASO DE PRUEBA: Búsqueda de empleado por ID - comprobar que se recupera correctamente si existe
    @Test
    public void testBuscarEmpleado() {
        Empleado empleado = Empleado.builder()
                .nombre("Marcos Rivas")
                .email("marcos.rivas@smartfenix.com")
                .rol("DevOps Engineer")
                .build();
        Empleado guardado = empleadoService.save(empleado);

        Optional<Empleado> encontrado = empleadoService.findById(guardado.getId());

        assertTrue(encontrado.isPresent());
        assertEquals("Marcos Rivas", encontrado.get().getNombre());
        assertEquals("marcos.rivas@smartfenix.com", encontrado.get().getEmail());
    }

    // CASO DE PRUEBA: Actualización de empleado - comprobar que los campos modificados se actualizan correctamente
    @Test
    public void testActualizarEmpleado() {
        Empleado empleado = Empleado.builder()
                .nombre("Sofía Castro")
                .email("sofia.castro@smartfenix.com")
                .rol("Soporte Técnico")
                .build();
        Empleado guardado = empleadoService.save(empleado);

        guardado.setNombre("Sofía Castro Rivas");
        guardado.setRol("Líder de Soporte");

        Optional<Empleado> actualizadoOpt = empleadoService.update(guardado.getId(), guardado);

        assertTrue(actualizadoOpt.isPresent());
        Empleado actualizado = actualizadoOpt.get();
        assertEquals("Sofía Castro Rivas", actualizado.getNombre());
        assertEquals("Líder de Soporte", actualizado.getRol());
        assertEquals("sofia.castro@smartfenix.com", actualizado.getEmail());
    }
}
