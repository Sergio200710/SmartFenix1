package com.smartfenix.service;

import com.smartfenix.domain.Cliente;
import com.smartfenix.exception.RegistroNoEncontradoException;
import com.smartfenix.exception.RegistroRelacionadoException;
import com.smartfenix.repository.ClienteRepository;
import com.smartfenix.repository.ProyectoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ProyectoRepository proyectoRepository;

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> findById(Long id) {
        return clienteRepository.findById(id);
    }

    public Cliente save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Optional<Cliente> update(Long id, Cliente cliente) {
        return clienteRepository.findById(id).map(existingCliente -> {
            existingCliente.setNombre(cliente.getNombre());
            existingCliente.setTelefono(cliente.getTelefono());
            existingCliente.setEmpresa(cliente.getEmpresa());
            return clienteRepository.save(existingCliente);
        });
    }

    public boolean tieneProyectosAsociados(Long clienteId) {
        return proyectoRepository.existsByClienteId(clienteId);
    }

    public void delete(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new RegistroNoEncontradoException("Cliente no encontrado.");
        }
        if (tieneProyectosAsociados(id)) {
            throw new RegistroRelacionadoException("No se puede eliminar el cliente porque tiene proyectos asociados.");
        }
        clienteRepository.deleteById(id);
    }
}
