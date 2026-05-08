package com.smartfenix;

import com.smartfenix.domain.Cliente;
import com.smartfenix.domain.Empleado;
import com.smartfenix.domain.Proyecto;
import com.smartfenix.domain.Tarea;
import com.smartfenix.repository.ClienteRepository;
import com.smartfenix.repository.EmpleadoRepository;
import com.smartfenix.repository.ProyectoRepository;
import com.smartfenix.repository.TareaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final ClienteRepository clienteRepository;
    private final ProyectoRepository proyectoRepository;
    private final EmpleadoRepository empleadoRepository;
    private final TareaRepository tareaRepository;

    @Override
    public void run(String... args) throws Exception {
        // Para que no se inserten duplicados si ya existen datos
        if (clienteRepository.count() == 0) {
            
            // 1. Crear Clientes
            Cliente c1 = Cliente.builder().nombre("Tech Global").empresa("Tech Global Corp").telefono("600123456").build();
            Cliente c2 = Cliente.builder().nombre("Innova Solutions").empresa("Innova SL").telefono("600987654").build();
            clienteRepository.save(c1);
            clienteRepository.save(c2);

            // 2. Crear Proyectos
            Proyecto p1 = Proyecto.builder().nombre("Migración Cloud").fechaInicio(LocalDate.now().minusDays(10)).fechaFin(LocalDate.now().plusDays(30)).cliente(c1).build();
            Proyecto p2 = Proyecto.builder().nombre("App Móvil E-commerce").fechaInicio(LocalDate.now()).fechaFin(LocalDate.now().plusDays(60)).cliente(c2).build();
            proyectoRepository.save(p1);
            proyectoRepository.save(p2);

            // 3. Crear Empleados
            Empleado e1 = Empleado.builder().nombre("Ana García").email("ana.garcia@smartfenix.com").rol("Desarrolladora Backend").build();
            Empleado e2 = Empleado.builder().nombre("Carlos López").email("carlos.lopez@smartfenix.com").rol("Jefe de Proyecto").build();
            Empleado e3 = Empleado.builder().nombre("Laura Martínez").email("laura.martinez@smartfenix.com").rol("Diseñadora UX/UI").build();
            empleadoRepository.save(e1);
            empleadoRepository.save(e2);
            empleadoRepository.save(e3);

            // 4. Crear Tareas
            Tarea t1 = Tarea.builder().descripcion("Configurar base de datos AWS").estado("En progreso").proyecto(p1).empleado(e1).build();
            Tarea t2 = Tarea.builder().descripcion("Diseñar wireframes").estado("Completada").proyecto(p2).empleado(e3).build();
            Tarea t3 = Tarea.builder().descripcion("Revisión de requisitos").estado("Pendiente").proyecto(p2).empleado(e2).build();
            tareaRepository.save(t1);
            tareaRepository.save(t2);
            tareaRepository.save(t3);

            System.out.println("✅ Datos de prueba insertados en la base de datos.");
        } else {
            System.out.println("✅ La base de datos ya contiene información. No se insertaron datos iniciales.");
        }
    }
}
