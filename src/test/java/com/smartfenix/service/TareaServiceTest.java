package com.smartfenix.service;

import com.smartfenix.domain.Empleado;
import com.smartfenix.domain.Proyecto;
import com.smartfenix.domain.Tarea;
import com.smartfenix.exception.RegistroNoEncontradoException;
import com.smartfenix.repository.TareaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TareaServiceTest {

    @Mock
    private TareaRepository tareaRepository;

    @InjectMocks
    private TareaService tareaService;

    @Test
    public void testCrearTarea() {
        Tarea tarea = Tarea.builder()
                .id(1L)
                .descripcion("Preparar despliegue")
                .estado("Pendiente")
                .build();

        when(tareaRepository.save(tarea)).thenReturn(tarea);

        Tarea guardada = tareaService.save(tarea);

        assertNotNull(guardada.getId());
        assertEquals("Preparar despliegue", guardada.getDescripcion());
        assertEquals("Pendiente", guardada.getEstado());
        verify(tareaRepository).save(tarea);
    }

    @Test
    public void testListarTareas() {
        List<Tarea> tareas = List.of(
                Tarea.builder().id(1L).descripcion("Preparar despliegue").estado("Pendiente").build(),
                Tarea.builder().id(2L).descripcion("Revisar incidencias").estado("En progreso").build()
        );

        when(tareaRepository.findAll()).thenReturn(tareas);

        List<Tarea> resultado = tareaService.findAll();

        assertEquals(2, resultado.size());
        verify(tareaRepository).findAll();
    }

    @Test
    public void testEliminarTarea() {
        Long id = 9L;
        when(tareaRepository.existsById(id)).thenReturn(true);

        tareaService.delete(id);

        verify(tareaRepository).deleteById(id);
    }

    @Test
    public void testEliminarTareaNoExistente() {
        Long id = 99L;
        when(tareaRepository.existsById(id)).thenReturn(false);

        RegistroNoEncontradoException exception = assertThrows(
                RegistroNoEncontradoException.class,
                () -> tareaService.delete(id)
        );

        assertEquals("Tarea no encontrada.", exception.getMessage());
        verify(tareaRepository, never()).deleteById(id);
    }

    @Test
    public void testActualizarTarea() {
        Proyecto proyecto = Proyecto.builder().id(3L).nombre("Proyecto Demo").build();
        Empleado empleado = Empleado.builder().id(4L).nombre("Ana").email("ana@test.com").rol("QA").build();
        Tarea tareaExistente = Tarea.builder()
                .id(1L)
                .descripcion("Preparar despliegue")
                .estado("Pendiente")
                .build();
        Tarea datosActualizados = Tarea.builder()
                .descripcion("Desplegar version final")
                .estado("Completada")
                .proyecto(proyecto)
                .empleado(empleado)
                .build();

        when(tareaRepository.findById(1L)).thenReturn(Optional.of(tareaExistente));
        when(tareaRepository.save(any(Tarea.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<Tarea> tareaActualizadaOpt = tareaService.update(1L, datosActualizados);

        assertTrue(tareaActualizadaOpt.isPresent());
        Tarea tareaActualizada = tareaActualizadaOpt.get();
        assertEquals("Desplegar version final", tareaActualizada.getDescripcion());
        assertEquals("Completada", tareaActualizada.getEstado());
        assertEquals(proyecto, tareaActualizada.getProyecto());
        assertEquals(empleado, tareaActualizada.getEmpleado());
        verify(tareaRepository).findById(1L);
        verify(tareaRepository).save(tareaExistente);
    }
}
