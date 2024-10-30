package com.api.adm.controllers;

import com.api.adm.entity.Cliente;
import com.api.adm.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/clientes")
public class ClienteViewController {

    @Autowired
    private ClienteService clienteService;

    // Listar todos los clientes
    @GetMapping
    public String listarClientes(Model model) {
        model.addAttribute("clientes", clienteService.obtenerTodosLosClientes());
        return "clientes";  // Vista 'clientes.html'
    }

    // Mostrar formulario para agregar un nuevo cliente
    @GetMapping("/nuevo")
    public String mostrarFormularioCliente(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "formulario_cliente";  // Vista 'formulario_cliente.html'
    }

    // Guardar un nuevo cliente
    @PostMapping("/guardar")
    public String guardarCliente(@Valid @ModelAttribute("cliente") Cliente cliente, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "formulario_cliente";
        }
        clienteService.crearCliente(cliente);
        return "redirect:/clientes?success=created";
    }

    // Mostrar formulario para editar un cliente existente
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        Cliente cliente = clienteService.obtenerClientePorId(id);
        model.addAttribute("cliente", cliente);
        return "formulario_cliente";  // Reutiliza 'formulario_cliente.html'
    }

    // Actualizar un cliente existente
    @PostMapping("/actualizar/{id}")
    public String actualizarCliente(@PathVariable Long id, @Valid @ModelAttribute("cliente") Cliente cliente, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "formulario_cliente";
        }
        clienteService.actualizarCliente(id, cliente);
        return "redirect:/clientes?success=updated";
    }

    // Eliminar un cliente
    @PostMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
        return "redirect:/clientes?success=deleted";
    }
}

