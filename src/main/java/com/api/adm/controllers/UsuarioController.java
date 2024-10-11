package com.api.adm.controllers;

import com.api.adm.entity.Usuario;
import com.api.adm.service.UsuarioService;
import com.api.adm.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/registro")
    public String mostrarFormularioDeRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute("usuario") Usuario usuario) {
        // Encriptar la contraseña antes de guardar
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioService.guardarUsuario(usuario);

        // Enviar correo de bienvenida
        String asunto = "¡Bienvenido a nuestra aplicación!";
        String mensaje = String.format(
                "Hola %s,\n\nGracias por registrarte en nuestra aplicación. Esperamos que disfrutes de la experiencia.\n\nSaludos,\nEl equipo de la aplicación.",
                usuario.getUsername()
        );
        emailService.enviarEmailDeConfirmacion(usuario.getEmail(), asunto, mensaje);

        // Redirigir a la página de registro con un mensaje de éxito
        return "redirect:/usuarios/registro?success=true";
    }

    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.obtenerTodosLosUsuarios());
        return "usuarios";
    }
}





