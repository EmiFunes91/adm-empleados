package com.api.adm.service;

import com.api.adm.entity.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    void guardarUsuario(Usuario usuario);
    List<Usuario> obtenerTodosLosUsuarios();
    boolean existePorEmail(String email);
    boolean existePorUsername(String username);
    Optional<Usuario> buscarPorUsername(String username);
    Optional<Usuario> obtenerUsuarioPorId(Long id);
    void eliminarUsuarioPorId(Long id);

    // Nuevo método para obtener usuarios por rol
    List<Usuario> obtenerUsuariosPorRol(String rolName);
}







