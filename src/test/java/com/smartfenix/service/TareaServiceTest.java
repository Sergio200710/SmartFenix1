package com.smartfenix.service;

import com.smartfenix.domain.Tarea;
import com.smartfenix.repository.TareaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TareaServiceTest {

    @Mock
    private TareaRepository tareaRepository;

    @InjectMocks
    private TareaService tareaService;

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
}
