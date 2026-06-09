package com.smartfenix.service;

import com.smartfenix.domain.Empleado;
import com.smartfenix.exception.RegistroRelacionadoException;
import com.smartfenix.repository.EmpleadoRepository;
import com.smartfenix.repository.TareaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmpleadoServiceTest {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @Mock
    private TareaRepository tareaRepository;

    @InjectMocks
    private EmpleadoService empleadoService;

    @Test
    public void testCrearEmpleado() {
        Empleado empleado = Empleado.builder()
                .id(1L)
                .nombre("Laura Montes")
                .email("laura.montes@smartfenix.com")
                .rol("Diseñadora UI/UX")
                .build();

        when(empleadoRepository.save(empleado)).thenReturn(empleado);

        Empleado guardado = empleadoService.save(empleado);

        assertNotNull(guardado.getId());
        assertEquals("Laura Montes", guardado.getNombre());
        assertEquals("laura.montes@smartfenix.com", guardado.getEmail());
        assertEquals("Diseñadora UI/UX", guardado.getRol());
        verify(empleadoRepository).save(empleado);
    }

    @Test
    public void testListarEmpleados() {
        List<Empleado> empleados = List.of(
                Empleado.builder().id(1L).nombre("Marcos Rivas").email("marcos.rivas@smartfenix.com").rol("DevOps Engineer").build(),
                Empleado.builder().id(2L).nombre("Sofia Castro").email("sofia.castro@smartfenix.com").rol("Soporte").build()
        );

        when(empleadoRepository.findAll()).thenReturn(empleados);

        List<Empleado> resultado = empleadoService.findAll();

        assertEquals(2, resultado.size());
        assertEquals("Marcos Rivas", resultado.get(0).getNombre());
        verify(empleadoRepository).findAll();
    }

    @Test
    public void testEliminarEmpleado() {
        Long id = 7L;
        when(empleadoRepository.existsById(id)).thenReturn(true);
        when(tareaRepository.existsByEmpleadoId(id)).thenReturn(false);

        empleadoService.delete(id);

        verify(empleadoRepository).deleteById(id);
    }

    @Test
    public void testNoEliminarEmpleadoConTareasAsociadas() {
        Long id = 8L;
        when(empleadoRepository.existsById(id)).thenReturn(true);
        when(tareaRepository.existsByEmpleadoId(id)).thenReturn(true);

        RegistroRelacionadoException exception = assertThrows(
                RegistroRelacionadoException.class,
                () -> empleadoService.delete(id)
        );

        assertEquals("No se puede eliminar el empleado porque tiene tareas asignadas.", exception.getMessage());
        verify(empleadoRepository, never()).deleteById(id);
    }
}
