package com.smartfenix.service;

import com.smartfenix.domain.Proyecto;
import com.smartfenix.repository.ProyectoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProyectoService {

    private final ProyectoRepository proyectoRepository;

    public List<Proyecto> findAll() {
        return proyectoRepository.findAll();
    }

    public Optional<Proyecto> findById(Long id) {
        return proyectoRepository.findById(id);
    }

    public Proyecto save(Proyecto proyecto) {
        return proyectoRepository.save(proyecto);
    }

    public Optional<Proyecto> update(Long id, Proyecto proyecto) {
        return proyectoRepository.findById(id).map(existingProyecto -> {
            existingProyecto.setNombre(proyecto.getNombre());
            existingProyecto.setFechaInicio(proyecto.getFechaInicio());
            existingProyecto.setFechaFin(proyecto.getFechaFin());
            existingProyecto.setCliente(proyecto.getCliente());
            return proyectoRepository.save(existingProyecto);
        });
    }

    public void delete(Long id) {
        proyectoRepository.deleteById(id);
    }
}
