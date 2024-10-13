package com.api.adm.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.api.adm.entity.Role;
import com.api.adm.entity.Usuario;
import com.api.adm.repository.UsuarioRepository;
import com.api.adm.service.RoleService;
import com.api.adm.service.UsuarioService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RoleService roleService;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, RoleService roleService) {
        this.usuarioRepository = usuarioRepository;
        this.roleService = roleService;
    }

    @Override
    @Transactional  // Asegura que todas las operaciones ocurran dentro de una transacción
    public void guardarUsuario(Usuario usuario) {
        Set<Role> rolesPersistidos = new HashSet<>();

        usuario.getRoles().forEach(role -> {
            Optional<Role> existingRole = roleService.buscarPorNombre(role.getName());
            if (existingRole.isPresent()) {
                rolesPersistidos.add(roleService.merge(existingRole.get()));
            } else {
                Role nuevoRole = roleService.guardar(role);
                rolesPersistidos.add(nuevoRole);
            }
        });

        usuario.setRoles(rolesPersistidos);
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
}










