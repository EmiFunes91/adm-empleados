package com.api.adm.service;

import com.api.adm.dto.UsuarioDTO;
import com.api.adm.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    void guardarUsuario(Usuario usuario, String roleName);
    List<Usuario> obtenerTodosLosUsuarios();
    boolean existePorEmail(String email);
    boolean existePorUsername(String username);
    Optional<Usuario> buscarPorUsername(String username);
    Optional<Usuario> obtenerUsuarioPorId(Long id);
    void eliminarUsuarioPorId(Long id);
    List<Usuario> obtenerUsuariosPorRol(String rolName);
    Usuario registrarUsuario(UsuarioDTO usuarioDTO); // Cambié a devolver Usuario
    Usuario actualizarUsuario(Long id, UsuarioDTO usuarioDTO); // Cambié a devolver Usuario
}









