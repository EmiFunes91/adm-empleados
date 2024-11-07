package com.api.adm.controllers;

import com.api.adm.dto.RegistroUsuarioDTO;
import com.api.adm.entity.Usuario;
import com.api.adm.service.EmailService;
import com.api.adm.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/crear-cuenta")
public class RegistroController {

    private static final Logger logger = LoggerFactory.getLogger(RegistroController.class);

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public String registerUser(@ModelAttribute RegistroUsuarioDTO registroUsuarioDTO) {
        if (usuarioService.existePorUsername(registroUsuarioDTO.getUsername())) {
            return "redirect:/crear-cuenta?error=username_taken"; // Redirige si el usuario ya existe
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(registroUsuarioDTO.getUsername());
        nuevoUsuario.setPassword(passwordEncoder.encode(registroUsuarioDTO.getPassword()));
        nuevoUsuario.setEmail(registroUsuarioDTO.getEmail());
        nuevoUsuario.setActivo(false); // Inicialmente inactivo

        usuarioService.guardarUsuario(nuevoUsuario);

        // Enviar correo a soporte para la verificación
        String asunto = "Nueva solicitud de registro";
        String mensaje = "El usuario " + registroUsuarioDTO.getUsername() + " ha solicitado registrarse. " +
                "Por favor, verifica la cuenta haciendo clic en el siguiente enlace:\n" +
                "http://localhost:8080/activacion/" + nuevoUsuario.getId(); // Enlace para activar cuenta
        emailService.enviarEmailDeConfirmacion("soporte.funesapps@gmail.com", asunto, mensaje);

        return "redirect:/crear-cuenta?success=registered"; // Redirigir a la misma página con un mensaje de éxito
    }

}









