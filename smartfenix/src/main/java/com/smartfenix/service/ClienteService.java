package com.smartfenix.service;

import com.smartfenix.domain.Cliente;
import com.smartfenix.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

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

    public void delete(Long id) {
        clienteRepository.deleteById(id);
    }
}
