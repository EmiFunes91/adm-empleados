package com.api.adm.controllers;

import com.api.adm.dto.RegistroUsuarioDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/crear-cuenta")
public class RegistroViewController {

    private static final Logger logger = LoggerFactory.getLogger(RegistroViewController.class);

    @GetMapping
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("registroUsuarioDTO", new RegistroUsuarioDTO());
        logger.info("Accediendo al formulario de registro.");
        return "crear_cuenta"; // Nombre de la vista para crear cuenta
    }
}


