package com.smartfenix.controller.web;

import com.smartfenix.domain.Proyecto;
import com.smartfenix.service.ClienteService;
import com.smartfenix.service.ProyectoService;
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
@RequestMapping("/proyectos")
@RequiredArgsConstructor
public class ProyectoWebController {

    private final ProyectoService proyectoService;
    private final ClienteService clienteService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("proyectos", proyectoService.findAll());
        return "proyectos/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        cargarRelacionados(model);
        model.addAttribute("proyecto", new Proyecto());
        model.addAttribute("clienteSeleccionadoId", null);
        model.addAttribute("modoEdicion", false);
        return "proyectos/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("proyecto") Proyecto proyecto,
                          BindingResult bindingResult,
                          @RequestParam(required = false) Long clienteId,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if (bindingResult.hasErrors()) {
            cargarRelacionados(model);
            model.addAttribute("clienteSeleccionadoId", clienteId);
            model.addAttribute("modoEdicion", false);
            return "proyectos/formulario";
        }
        if (clienteId != null) {
            clienteService.findById(clienteId).ifPresent(proyecto::setCliente);
        }
        proyectoService.save(proyecto);
        redirectAttributes.addFlashAttribute("mensaje", "Proyecto guardado correctamente.");
        return "redirect:/proyectos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return proyectoService.findById(id)
                .map(proyecto -> {
                    cargarRelacionados(model);
                    model.addAttribute("proyecto", proyecto);
                    model.addAttribute("clienteSeleccionadoId",
                            proyecto.getCliente() != null ? proyecto.getCliente().getId() : null);
                    model.addAttribute("modoEdicion", true);
                    return "proyectos/formulario";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Proyecto no encontrado.");
                    return "redirect:/proyectos";
                });
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("proyecto") Proyecto proyecto,
                             BindingResult bindingResult,
                             @RequestParam(required = false) Long clienteId,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        if (bindingResult.hasErrors()) {
            cargarRelacionados(model);
            model.addAttribute("clienteSeleccionadoId", clienteId);
            model.addAttribute("modoEdicion", true);
            return "proyectos/formulario";
        }
        if (clienteId != null) {
            clienteService.findById(clienteId).ifPresent(proyecto::setCliente);
        } else {
            proyecto.setCliente(null);
        }
        proyectoService.update(id, proyecto);
        redirectAttributes.addFlashAttribute("mensaje", "Proyecto actualizado correctamente.");
        return "redirect:/proyectos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        proyectoService.delete(id);
        redirectAttributes.addFlashAttribute("mensaje", "Proyecto eliminado correctamente.");
        return "redirect:/proyectos";
    }

    private void cargarRelacionados(Model model) {
        model.addAttribute("clientes", clienteService.findAll());
    }
}
