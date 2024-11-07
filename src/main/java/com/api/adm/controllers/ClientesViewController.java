package com.api.adm.controllers;

import com.api.adm.service.ClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/clientes")
public class ClientesViewController {

    private final ClienteService clienteService;

    public ClientesViewController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public String mostrarClientes(Model model) {
        model.addAttribute("clientes", clienteService.obtenerTodosLosClientes());
        return "clientes";
    }
}
