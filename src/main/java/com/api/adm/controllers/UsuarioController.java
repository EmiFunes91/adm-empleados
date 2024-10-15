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
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    // Lista todos los usuarios (solo para ADMIN)
    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.obtenerTodosLosUsuarios());
        return "usuarios";
    }

    @GetMapping("/registro")
    public String mostrarFormularioDeRegistro(Model model) {
        model.addAttribute("usuarioDTO", new UsuarioDTO());

        // Obtener todos los roles (ADMIN, USER)
        List<Role> rolesDisponibles = roleService.obtenerTodosLosRoles();
        model.addAttribute("roles", rolesDisponibles);
        return "registro";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@Valid @ModelAttribute("usuarioDTO") UsuarioDTO usuarioDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "registro";
        }

        if (usuarioService.existePorEmail(usuarioDTO.getEmail())) {
            model.addAttribute("error", "El correo electrónico ya está en uso.");
            return "registro";
        }

        if (usuarioService.existePorUsername(usuarioDTO.getUsername())) {
            model.addAttribute("error", "El nombre de usuario ya está en uso.");
            return "registro";
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(usuarioDTO.getUsername());
        usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
        usuario.setEmail(usuarioDTO.getEmail());

        // Obtener y asignar el rol
        for (String roleName : usuarioDTO.getRoles()) {
            usuarioService.guardarUsuario(usuario, roleName);  // Ahora pasa el rol directamente
        }

        emailService.enviarEmailDeConfirmacion(usuario.getEmail(), "¡Bienvenido a nuestra aplicación!",
                String.format("Hola %s,\n\nGracias por registrarte en nuestra aplicación.", usuario.getUsername()));

        return "redirect:/usuarios?success=created";
    }

    // Editar usuario (solo ADMIN)
    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setUsername(usuario.getUsername());
        usuarioDTO.setEmail(usuario.getEmail());
        usuarioDTO.setRoles(usuario.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));

        model.addAttribute("usuarioDTO", usuarioDTO);
        List<Role> rolesDisponibles = roleService.obtenerTodosLosRoles();
        model.addAttribute("roles", rolesDisponibles);
        return "editarUsuario";
    }

    @PostMapping("/editar/{id}")
    public String actualizarUsuario(@PathVariable Long id, @Valid @ModelAttribute("usuarioDTO") UsuarioDTO usuarioDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "editarUsuario";
        }

        Usuario usuario = usuarioService.obtenerUsuarioPorId(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setUsername(usuarioDTO.getUsername());
        usuario.setEmail(usuarioDTO.getEmail());

        // Actualizar roles
        for (String roleName : usuarioDTO.getRoles()) {
            usuarioService.guardarUsuario(usuario, roleName);  // Actualizar con el nuevo método
        }

        return "redirect:/usuarios?success=updated";
    }

    // Eliminar usuario (solo ADMIN)
    @PostMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar si el usuario tiene el rol de ADMIN
        boolean esAdmin = usuario.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN"));

        // Verificar que no sea el último admin
        if (esAdmin) {
            List<Usuario> administradores = usuarioService.obtenerUsuariosPorRol("ADMIN");
            if (administradores.size() == 1) {
                model.addAttribute("error", "No puedes eliminar el único administrador.");
                return "usuarios"; // Regresar a la lista de usuarios con el error
            }
        }

        usuarioService.eliminarUsuarioPorId(id);
        return "redirect:/usuarios?success=deleted";
    }
}


















