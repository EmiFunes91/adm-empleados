package com.api.adm.controllers;

import com.api.adm.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/recuperar-password")
public class RecuperacionController {

    @Autowired
    private UsuarioService usuarioService;

    // Muestra el formulario para ingresar el correo
    @GetMapping
    public String mostrarFormularioRecuperarPassword(Model model) {
        return "recuperar_password";
    }

    // Procesa la solicitud de recuperación
    @PostMapping
    public String procesarRecuperacionPassword(@RequestParam("email") String email, Model model) {
        try {
            usuarioService.solicitarRecuperacionPassword(email);
            model.addAttribute("message", "Hemos enviado un enlace para restablecer tu contraseña a tu correo.");
            return "recuperar_password";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "recuperar_password";
        }
    }

    // Muestra el formulario de restablecimiento de contraseña
    @GetMapping("/reset")
    public String mostrarFormularioRestablecerPassword(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "restablecer_password";
    }

    // Procesa el restablecimiento de contraseña
    @PostMapping("/reset")
    public String procesarRestablecimientoPassword(
            @RequestParam("token") String token,
            @RequestParam("password") String nuevaPassword,
            Model model) {
        try {
            usuarioService.restablecerPassword(token, nuevaPassword);
            model.addAttribute("message", "Contraseña restablecida con éxito. Ahora puedes iniciar sesión.");
            return "login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "restablecer_password";
        }
    }
}
