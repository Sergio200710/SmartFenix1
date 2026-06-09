package com.smartfenix.controller.web;

import com.smartfenix.domain.Empleado;
import com.smartfenix.service.EmpleadoService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/empleados")
@RequiredArgsConstructor
public class EmpleadoWebController {

    private final EmpleadoService empleadoService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("empleados", empleadoService.findAll());
        return "empleados/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("empleado", new Empleado());
        model.addAttribute("modoEdicion", false);
        return "empleados/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("empleado") Empleado empleado,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("modoEdicion", false);
            return "empleados/formulario";
        }
        empleadoService.save(empleado);
        redirectAttributes.addFlashAttribute("mensaje", "Empleado guardado correctamente.");
        return "redirect:/empleados";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return empleadoService.findById(id)
                .map(empleado -> {
                    model.addAttribute("empleado", empleado);
                    model.addAttribute("modoEdicion", true);
                    return "empleados/formulario";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Empleado no encontrado.");
                    return "redirect:/empleados";
                });
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("empleado") Empleado empleado,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("modoEdicion", true);
            return "empleados/formulario";
        }
        empleadoService.update(id, empleado);
        redirectAttributes.addFlashAttribute("mensaje", "Empleado actualizado correctamente.");
        return "redirect:/empleados";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        empleadoService.delete(id);
        redirectAttributes.addFlashAttribute("mensaje", "Empleado eliminado correctamente.");
        return "redirect:/empleados";
    }
}
