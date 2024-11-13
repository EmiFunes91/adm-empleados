package com.api.adm.controllers;

import com.api.adm.entity.Cliente;
import com.api.adm.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/clientes")
public class ClienteViewController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public String listarClientes(@RequestParam(value = "query", required = false) String query, Model model) {
        List<Cliente> clientes = (query != null && !query.isEmpty())
                ? clienteService.buscarClientesPorNombreOApellido(query)
                : clienteService.obtenerTodosLosClientes();
        model.addAttribute("clientes", clientes);
        model.addAttribute("query", query);
        return "clientes";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioCrearCliente(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "formulario_cliente";
    }

    @PostMapping("/guardar")
    public String guardarCliente(@Valid @ModelAttribute("cliente") Cliente cliente, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("cliente", cliente);
            return "formulario_cliente";
        }
        clienteService.crearCliente(cliente);
        return "redirect:/clientes?success=created";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarCliente(@PathVariable Long id, Model model) {
        Cliente cliente = clienteService.obtenerClientePorId(id);
        if (cliente == null) {
            return "redirect:/clientes?error=notfound";
        }
        model.addAttribute("cliente", cliente);
        return "editar_clientes";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarCliente(@PathVariable Long id, @Valid @ModelAttribute("cliente") Cliente clienteActualizado, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("cliente", clienteActualizado);
            return "editar_clientes";
        }
        clienteService.actualizarCliente(id, clienteActualizado);
        return "redirect:/clientes?success=updated";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
        return "redirect:/clientes?success=deleted";
    }
}

