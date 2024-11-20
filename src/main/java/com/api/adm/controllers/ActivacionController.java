package com.api.adm.controllers;

import com.api.adm.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/activacion")
public class ActivacionController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String activarCuenta(@RequestParam("token") String token, Model model) {
        try {
            usuarioService.activarCuentaConToken(token);
            model.addAttribute("message", "Cuenta activada con éxito. Ahora puedes iniciar sesión.");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "login"; // Vista de login
    }
}

