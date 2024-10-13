package com.api.adm.controllers;

import com.api.adm.dto.UsuarioDTO;
import com.api.adm.entity.Role;
import com.api.adm.entity.Usuario;
import com.api.adm.service.RoleService;
import com.api.adm.service.UsuarioService;
import com.api.adm.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    @GetMapping("/registro")
    public String mostrarFormularioDeRegistro(Model model) {
        model.addAttribute("usuarioDTO", new UsuarioDTO());

        // Cargar todos los roles desde el servicio
        List<Role> rolesDisponibles = roleService.obtenerTodosLosRoles();
        model.addAttribute("roles", rolesDisponibles);

        return "registro";
    }


    @PostMapping("/guardar")
    public String guardarUsuario(@Valid @ModelAttribute("usuarioDTO") UsuarioDTO usuarioDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<Role> rolesDisponibles = roleService.obtenerTodosLosRoles();
            model.addAttribute("roles", rolesDisponibles); // Asegúrate de recargar los roles en caso de error
            return "registro";
        }

        if (usuarioService.existePorEmail(usuarioDTO.getEmail())) {
            model.addAttribute("error", "El correo electrónico ya está en uso.");
            List<Role> rolesDisponibles = roleService.obtenerTodosLosRoles();
            model.addAttribute("roles", rolesDisponibles); // Recargar roles si hay error
            return "registro";
        }

        if (usuarioService.existePorUsername(usuarioDTO.getUsername())) {
            model.addAttribute("error", "El nombre de usuario ya está en uso.");
            List<Role> rolesDisponibles = roleService.obtenerTodosLosRoles();
            model.addAttribute("roles", rolesDisponibles); // Recargar roles si hay error
            return "registro";
        }

        try {
            Usuario usuario = new Usuario();
            usuario.setUsername(usuarioDTO.getUsername());
            usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
            usuario.setEmail(usuarioDTO.getEmail());

            // Asignar roles seleccionados al usuario
            Set<Role> roles = new HashSet<>();
            for (String roleName : usuarioDTO.getRoles()) {
                Role role = roleService.buscarPorNombre(roleName).orElseThrow(() -> new RuntimeException("Rol no encontrado: " + roleName));
                roles.add(role);
            }
            usuario.setRoles(roles);

            usuarioService.guardarUsuario(usuario);

            String asunto = "¡Bienvenido a nuestra aplicación!";
            String mensaje = String.format(
                    "Hola %s,\n\nGracias por registrarte en nuestra aplicación. Esperamos que disfrutes de la experiencia.\n\nSaludos,\nEl equipo de la aplicación.",
                    usuario.getUsername()
            );
            emailService.enviarEmailDeConfirmacion(usuario.getEmail(), asunto, mensaje);

            return "redirect:/usuarios/registro?success=true";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Ocurrió un problema al registrar el usuario.");
            List<Role> rolesDisponibles = roleService.obtenerTodosLosRoles();
            model.addAttribute("roles", rolesDisponibles); // Recargar roles si hay excepción
            return "registro";
        }
    }

    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.obtenerTodosLosUsuarios());
        return "usuarios";
    }
}















