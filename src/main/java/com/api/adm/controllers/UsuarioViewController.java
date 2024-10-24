package com.api.adm.controllers;

import com.api.adm.dto.UsuarioDTO;
import com.api.adm.entity.Role;
import com.api.adm.entity.Usuario;
import com.api.adm.service.RoleService;
import com.api.adm.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioViewController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RoleService roleService;

    @GetMapping
    public String listarUsuarios(@RequestParam(value = "query", required = false) String query, Model model) {
        List<Usuario> usuarios;
        if (query != null && !query.isEmpty()) {
            usuarios = usuarioService.buscarUsuarios(query);
        } else {
            usuarios = usuarioService.obtenerTodosLosUsuarios();
        }
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("query", query);
        return "usuarios"; // Nombre de la vista
    }

    @GetMapping("/registro")
    public String mostrarFormularioDeRegistro(Model model) {
        model.addAttribute("usuarioDTO", new UsuarioDTO());
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

        usuarioService.registrarUsuario(usuarioDTO);
        return "redirect:/usuarios?success=created";
    }

    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setUsername(usuario.getUsername());
        usuarioDTO.setEmail(usuario.getEmail());
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

        usuarioService.actualizarUsuario(id, usuarioDTO);
        return "redirect:/usuarios?success=updated";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuarioPorId(id);
        return "redirect:/usuarios?success=deleted";
    }
}







