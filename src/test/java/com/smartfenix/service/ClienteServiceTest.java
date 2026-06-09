package com.smartfenix.service;

import com.smartfenix.domain.Cliente;
import com.smartfenix.exception.RegistroRelacionadoException;
import com.smartfenix.repository.ClienteRepository;
import com.smartfenix.repository.ProyectoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ProyectoRepository proyectoRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    public void testCrearCliente() {
        Cliente cliente = Cliente.builder()
                .id(1L)
                .nombre("Amazon")
                .empresa("Amazon Web Services")
                .telefono("123456789")
                .build();

        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente guardado = clienteService.save(cliente);

        assertNotNull(guardado.getId());
        assertEquals("Amazon", guardado.getNombre());
        assertEquals("Amazon Web Services", guardado.getEmpresa());
        verify(clienteRepository).save(cliente);
    }

    @Test
    public void testListarClientes() {
        List<Cliente> clientes = List.of(
                Cliente.builder().id(1L).nombre("Sony").empresa("Sony Europe").telefono("987654321").build(),
                Cliente.builder().id(2L).nombre("Google").empresa("Google LLC").telefono("111222333").build()
        );

        when(clienteRepository.findAll()).thenReturn(clientes);

        List<Cliente> resultado = clienteService.findAll();

        assertEquals(2, resultado.size());
        assertEquals("Sony", resultado.get(0).getNombre());
        verify(clienteRepository).findAll();
    }

    @Test
    public void testEliminarCliente() {
        Long id = 5L;
        when(clienteRepository.existsById(id)).thenReturn(true);
        when(proyectoRepository.existsByClienteId(id)).thenReturn(false);

        clienteService.delete(id);

        verify(clienteRepository).deleteById(id);
    }

    @Test
    public void testNoEliminarClienteConProyectosAsociados() {
        Long id = 7L;
        when(clienteRepository.existsById(id)).thenReturn(true);
        when(proyectoRepository.existsByClienteId(id)).thenReturn(true);

        RegistroRelacionadoException exception = assertThrows(
                RegistroRelacionadoException.class,
                () -> clienteService.delete(id)
        );

        assertEquals("No se puede eliminar el cliente porque tiene proyectos asociados.", exception.getMessage());
        verify(clienteRepository, never()).deleteById(id);
    }
}
