package com.smartfenix.service;

import com.smartfenix.domain.Proyecto;
import com.smartfenix.repository.ProyectoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProyectoServiceTest {

    @Mock
    private ProyectoRepository proyectoRepository;

    @InjectMocks
    private ProyectoService proyectoService;

    @Test
    public void testCrearProyecto() {
        Proyecto proyecto = Proyecto.builder()
                .id(1L)
                .nombre("Proyecto Migración AWS")
                .fechaInicio(LocalDate.now())
                .fechaFin(LocalDate.now().plusDays(45))
                .build();

        when(proyectoRepository.save(proyecto)).thenReturn(proyecto);

        Proyecto guardado = proyectoService.save(proyecto);

        assertNotNull(guardado.getId());
        assertEquals("Proyecto Migración AWS", guardado.getNombre());
        verify(proyectoRepository).save(proyecto);
    }

    @Test
    public void testListarProyectos() {
        List<Proyecto> proyectos = List.of(
                Proyecto.builder().id(1L).nombre("Proyecto Playstation").fechaInicio(LocalDate.now()).fechaFin(LocalDate.now().plusDays(60)).build(),
                Proyecto.builder().id(2L).nombre("Proyecto ERP").fechaInicio(LocalDate.now()).fechaFin(LocalDate.now().plusDays(30)).build()
        );

        when(proyectoRepository.findAll()).thenReturn(proyectos);

        List<Proyecto> resultado = proyectoService.findAll();

        assertEquals(2, resultado.size());
        assertEquals("Proyecto Playstation", resultado.get(0).getNombre());
        verify(proyectoRepository).findAll();
    }

    @Test
    public void testActualizarProyecto() {
        Proyecto proyectoExistente = Proyecto.builder()
                .id(1L)
                .nombre("Proyecto Inicial")
                .fechaInicio(LocalDate.now())
                .fechaFin(LocalDate.now().plusDays(30))
                .build();
        Proyecto datosActualizados = Proyecto.builder()
                .nombre("Proyecto Actualizado")
                .fechaInicio(proyectoExistente.getFechaInicio())
                .fechaFin(proyectoExistente.getFechaFin().plusDays(15))
                .build();

        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyectoExistente));
        when(proyectoRepository.save(any(Proyecto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<Proyecto> proyectoActualizadoOpt = proyectoService.update(1L, datosActualizados);

        assertTrue(proyectoActualizadoOpt.isPresent());
        Proyecto proyectoActualizado = proyectoActualizadoOpt.get();
        assertEquals("Proyecto Actualizado", proyectoActualizado.getNombre());
        assertEquals(datosActualizados.getFechaFin(), proyectoActualizado.getFechaFin());
        verify(proyectoRepository).findById(1L);
        verify(proyectoRepository).save(proyectoExistente);
    }
}
