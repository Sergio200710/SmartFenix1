package com.smartfenix.service;

import com.smartfenix.domain.Empleado;
import com.smartfenix.repository.EmpleadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;

    public List<Empleado> findAll() {
        return empleadoRepository.findAll();
    }

    public Optional<Empleado> findById(Long id) {
        return empleadoRepository.findById(id);
    }

    public Empleado save(Empleado empleado) {
        return empleadoRepository.save(empleado);
    }

    public Optional<Empleado> update(Long id, Empleado empleado) {
        return empleadoRepository.findById(id).map(existingEmpleado -> {
            existingEmpleado.setNombre(empleado.getNombre());
            existingEmpleado.setEmail(empleado.getEmail());
            existingEmpleado.setRol(empleado.getRol());
            return empleadoRepository.save(existingEmpleado);
        });
    }

    public void delete(Long id) {
        empleadoRepository.deleteById(id);
    }
}
