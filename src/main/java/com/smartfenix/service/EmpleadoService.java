package com.smartfenix.service;

import com.smartfenix.domain.Empleado;
import com.smartfenix.exception.RegistroNoEncontradoException;
import com.smartfenix.exception.RegistroRelacionadoException;
import com.smartfenix.repository.EmpleadoRepository;
import com.smartfenix.repository.TareaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final TareaRepository tareaRepository;

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

    public boolean tieneTareasAsociadas(Long empleadoId) {
        return tareaRepository.existsByEmpleadoId(empleadoId);
    }

    public void delete(Long id) {
        if (!empleadoRepository.existsById(id)) {
            throw new RegistroNoEncontradoException("Empleado no encontrado.");
        }
        if (tieneTareasAsociadas(id)) {
            throw new RegistroRelacionadoException("No se puede eliminar el empleado porque tiene tareas asignadas.");
        }
        empleadoRepository.deleteById(id);
    }
}
