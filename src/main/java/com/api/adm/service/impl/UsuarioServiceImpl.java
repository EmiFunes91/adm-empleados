package com.api.adm.service.impl;

import com.api.adm.entity.Role;
import com.api.adm.entity.Usuario;
import com.api.adm.repository.UsuarioRepository;
import com.api.adm.service.RoleService;
import com.api.adm.service.UsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RoleService roleService;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, RoleService roleService) {
        this.usuarioRepository = usuarioRepository;
        this.roleService = roleService;
    }

    @Override
    @Transactional
    public void guardarUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    @Override
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public boolean existePorEmail(String email) {
        return usuarioRepository.findByEmail(email).isPresent();
    }

    @Override
    public boolean existePorUsername(String username) {
        return usuarioRepository.findByUsername(username).isPresent();
    }

    @Override
    public Optional<Usuario> buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public void eliminarUsuarioPorId(Long id) {
        usuarioRepository.deleteById(id);
    }

    // Método para obtener usuarios con un rol específico
    @Override
    public List<Usuario> obtenerUsuariosPorRol(String rolName) {
        return usuarioRepository.findUsuariosByRolName(rolName);
    }
}











