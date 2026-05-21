package com.smartfenix.service;

import com.smartfenix.domain.Cliente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ClienteServiceTest {

    @Autowired
    private ClienteService clienteService;

    // CASO DE PRUEBA: Alta de cliente - comprobar que el cliente se guarda correctamente en la base de datos
    @Test
    public void testCrearCliente() {
        Cliente cliente = Cliente.builder()
                .nombre("Amazon")
                .empresa("Amazon Web Services")
                .telefono("123456789")
                .build();

        Cliente guardado = clienteService.save(cliente);

        assertNotNull(guardado.getId());
        assertEquals("Amazon", guardado.getNombre());
        assertEquals("Amazon Web Services", guardado.getEmpresa());
    }

    // CASO DE PRUEBA: Búsqueda de cliente por ID - comprobar que un cliente se recupera correctamente si existe
    @Test
    public void testBuscarCliente() {
        Cliente cliente = Cliente.builder()
                .nombre("Sony")
                .empresa("Sony Europe")
                .telefono("987654321")
                .build();
        Cliente guardado = clienteService.save(cliente);

        Optional<Cliente> encontrado = clienteService.findById(guardado.getId());

        assertTrue(encontrado.isPresent());
        assertEquals("Sony", encontrado.get().getNombre());
    }

    // CASO DE PRUEBA: Actualización de cliente - comprobar que los campos modificados se actualizan correctamente
    @Test
    public void testActualizarCliente() {
        Cliente cliente = Cliente.builder()
                .nombre("Google")
                .empresa("Google España")
                .telefono("666555444")
                .build();
        Cliente guardado = clienteService.save(cliente);

        guardado.setNombre("Alphabet");
        guardado.setEmpresa("Alphabet Inc");

        Optional<Cliente> actualizadoOpt = clienteService.update(guardado.getId(), guardado);

        assertTrue(actualizadoOpt.isPresent());
        Cliente actualizado = actualizadoOpt.get();
        assertEquals("Alphabet", actualizado.getNombre());
        assertEquals("Alphabet Inc", actualizado.getEmpresa());
    }
}
