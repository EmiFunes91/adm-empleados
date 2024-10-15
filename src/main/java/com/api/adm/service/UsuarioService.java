package com.api.adm.service;

import com.api.adm.dto.UsuarioDTO;
import com.api.adm.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    Usuario guardarUsuario(Usuario usuario, String roleName);  // Método corregido con el nombre adecuado y el argumento roleName
    List<Usuario> obtenerTodosLosUsuarios();
    boolean existePorEmail(String email);
    boolean existePorUsername(String username);
    Optional<Usuario> buscarPorUsername(String username);
    Optional<Usuario> obtenerUsuarioPorId(Long id);
    void eliminarUsuarioPorId(Long id);
    List<Usuario> obtenerUsuariosPorRol(String rolName);  // Obtener usuarios por rol
    void registrarUsuario(UsuarioDTO usuarioDTO);  // Registro de usuario
    void actualizarUsuario(Long id, UsuarioDTO usuarioDTO);  // Actualización de usuario
}








