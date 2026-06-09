package com.smartfenix.controller.web;

import com.smartfenix.domain.Tarea;
import com.smartfenix.service.EmpleadoService;
import com.smartfenix.service.ProyectoService;
import com.smartfenix.service.TareaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tareas")
@RequiredArgsConstructor
public class TareaWebController {

    private final TareaService tareaService;
    private final ProyectoService proyectoService;
    private final EmpleadoService empleadoService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("tareas", tareaService.findAll());
        return "tareas/lista";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        cargarRelacionados(model);
        model.addAttribute("tarea", new Tarea());
        model.addAttribute("proyectoSeleccionadoId", null);
        model.addAttribute("empleadoSeleccionadoId", null);
        model.addAttribute("modoEdicion", false);
        return "tareas/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("tarea") Tarea tarea,
                          BindingResult bindingResult,
                          @RequestParam(required = false) Long proyectoId,
                          @RequestParam(required = false) Long empleadoId,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if (bindingResult.hasErrors()) {
            cargarRelacionados(model);
            model.addAttribute("proyectoSeleccionadoId", proyectoId);
            model.addAttribute("empleadoSeleccionadoId", empleadoId);
            model.addAttribute("modoEdicion", false);
            return "tareas/formulario";
        }
        asignarRelaciones(tarea, proyectoId, empleadoId);
        tareaService.save(tarea);
        redirectAttributes.addFlashAttribute("mensaje", "Tarea guardada correctamente.");
        return "redirect:/tareas";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return tareaService.findById(id)
                .map(tarea -> {
                    cargarRelacionados(model);
                    model.addAttribute("tarea", tarea);
                    model.addAttribute("proyectoSeleccionadoId",
                            tarea.getProyecto() != null ? tarea.getProyecto().getId() : null);
                    model.addAttribute("empleadoSeleccionadoId",
                            tarea.getEmpleado() != null ? tarea.getEmpleado().getId() : null);
                    model.addAttribute("modoEdicion", true);
                    return "tareas/formulario";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Tarea no encontrada.");
                    return "redirect:/tareas";
                });
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("tarea") Tarea tarea,
                             BindingResult bindingResult,
                             @RequestParam(required = false) Long proyectoId,
                             @RequestParam(required = false) Long empleadoId,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        if (bindingResult.hasErrors()) {
            cargarRelacionados(model);
            model.addAttribute("proyectoSeleccionadoId", proyectoId);
            model.addAttribute("empleadoSeleccionadoId", empleadoId);
            model.addAttribute("modoEdicion", true);
            return "tareas/formulario";
        }
        asignarRelaciones(tarea, proyectoId, empleadoId);
        tareaService.update(id, tarea);
        redirectAttributes.addFlashAttribute("mensaje", "Tarea actualizada correctamente.");
        return "redirect:/tareas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        tareaService.delete(id);
        redirectAttributes.addFlashAttribute("mensaje", "Tarea eliminada correctamente.");
        return "redirect:/tareas";
    }

    private void cargarRelacionados(Model model) {
        model.addAttribute("proyectos", proyectoService.findAll());
        model.addAttribute("empleados", empleadoService.findAll());
    }

    private void asignarRelaciones(Tarea tarea, Long proyectoId, Long empleadoId) {
        if (proyectoId != null) {
            proyectoService.findById(proyectoId).ifPresent(tarea::setProyecto);
        } else {
            tarea.setProyecto(null);
        }
        if (empleadoId != null) {
            empleadoService.findById(empleadoId).ifPresent(tarea::setEmpleado);
        } else {
            tarea.setEmpleado(null);
        }
    }
}
