package com.api.adm.controllers;

import com.api.adm.dto.UsuarioDTO;
import com.api.adm.entity.Role;
import com.api.adm.entity.Usuario;
import com.api.adm.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Lista todos los usuarios
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios(@RequestParam(value = "query", required = false) String query) {
        List<Usuario> usuarios;
        if (query != null && !query.isEmpty()) {
            usuarios = usuarioService.buscarUsuarios(query); // Llama al método para buscar usuarios
        } else {
            usuarios = usuarioService.obtenerTodosLosUsuarios();
        }
        List<UsuarioDTO> usuarioDTOs = usuarios.stream().map(this::convertToDto).collect(Collectors.toList());
        return ResponseEntity.ok(usuarioDTOs);
    }

    // Método para registrar usuario
    @PostMapping
    public ResponseEntity<UsuarioDTO> registrarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(null); // Manejo de errores
        }
        try {
            Usuario usuario = usuarioService.registrarUsuario(usuarioDTO);
            return ResponseEntity.ok(convertToDto(usuario));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null); // Manejo de errores
        }
    }

    // Método para editar usuario
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
        Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, usuarioDTO);
        return ResponseEntity.ok(convertToDto(usuarioActualizado));
    }

    // Método para eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuarioPorId(id);
        return ResponseEntity.noContent().build();
    }

    // Método auxiliar para convertir Usuario a UsuarioDTO
    private UsuarioDTO convertToDto(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setUsername(usuario.getUsername());
        dto.setEmail(usuario.getEmail());
        dto.setRoles(usuario.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet()));
        return dto;
    }
}




























