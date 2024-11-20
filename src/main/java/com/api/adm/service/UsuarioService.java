package com.api.adm.service;

import com.api.adm.dto.UsuarioDTO;
import com.api.adm.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    // Métodos para operaciones CRUD básicas
    void guardarUsuario(Usuario usuario);
    List<Usuario> obtenerTodosLosUsuarios();
    Optional<Usuario> obtenerUsuarioPorId(Long id);
    void eliminarUsuarioPorId(Long id);

    // Métodos para validación
    boolean existePorEmail(String email);
    boolean existePorUsername(String username);

    // Métodos de búsqueda
    List<Usuario> buscarUsuarios(String query);
    Optional<Usuario> buscarPorUsername(String username);
    List<Usuario> obtenerUsuariosPorRol(String rolName);

    // Métodos para creación y actualización de usuarios
    Usuario registrarUsuario(UsuarioDTO usuarioDTO);
    Usuario crearCuentaUsuario(UsuarioDTO usuarioDTO);
    Usuario actualizarUsuario(Long id, UsuarioDTO usuarioDTO);

    // Métodos para activación de cuenta
    void activarCuenta(Long id);
    void activarCuentaConToken(String token);
    void reenviarTokenActivacion(String email);

    // Métodos para recuperación de contraseña
    void solicitarRecuperacionPassword(String email);
    void restablecerPassword(String token, String nuevaPassword);
}














