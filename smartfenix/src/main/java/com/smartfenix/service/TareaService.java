package com.smartfenix.service;

import com.smartfenix.domain.Tarea;
import com.smartfenix.repository.TareaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TareaService {

    private final TareaRepository tareaRepository;

    public List<Tarea> findAll() {
        return tareaRepository.findAll();
    }

    public Optional<Tarea> findById(Long id) {
        return tareaRepository.findById(id);
    }

    public Tarea save(Tarea tarea) {
        return tareaRepository.save(tarea);
    }

    public Optional<Tarea> update(Long id, Tarea tarea) {
        return tareaRepository.findById(id).map(existingTarea -> {
            existingTarea.setDescripcion(tarea.getDescripcion());
            existingTarea.setEstado(tarea.getEstado());
            existingTarea.setProyecto(tarea.getProyecto());
            existingTarea.setEmpleado(tarea.getEmpleado());
            return tareaRepository.save(existingTarea);
        });
    }

    public void delete(Long id) {
        tareaRepository.deleteById(id);
    }
}
