package com.api.adm.controllers;

import com.api.adm.entity.Cliente;
import com.api.adm.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/clientes")
public class ClienteViewController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public String listarClientes(Model model) {
        model.addAttribute("clientes", clienteService.obtenerTodosLosClientes());
        return "clientes";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioCliente(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "formulario_cliente";
    }

    @PostMapping("/guardar")
    public String guardarCliente(@Valid @ModelAttribute("cliente") Cliente cliente, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "formulario_cliente";
        }
        clienteService.crearCliente(cliente);
        redirectAttributes.addFlashAttribute("success", "Cliente creado exitosamente.");
        return "redirect:/clientes";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Cliente cliente = clienteService.obtenerClientePorId(id);
        if (cliente == null) {
            redirectAttributes.addFlashAttribute("error", "Cliente no encontrado.");
            return "redirect:/clientes";
        }
        model.addAttribute("cliente", cliente);
        return "editar_clientes";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarCliente(
            @PathVariable Long id,
            @Valid @ModelAttribute("cliente") Cliente cliente,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            model.addAttribute("cliente", cliente);
            return "editar_clientes";
        }
        clienteService.actualizarCliente(id, cliente);
        redirectAttributes.addFlashAttribute("success", "Cambios guardados exitosamente.");
        return "redirect:/clientes";
    }


    @PostMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        clienteService.eliminarCliente(id);
        redirectAttributes.addFlashAttribute("success", "Cliente eliminado exitosamente.");
        return "redirect:/clientes";
    }
}




