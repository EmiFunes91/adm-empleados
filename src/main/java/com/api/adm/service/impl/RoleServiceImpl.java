package com.api.adm.service.impl;

import com.api.adm.entity.Role;
import com.api.adm.repository.RoleRepository;
import com.api.adm.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Optional<Role> buscarPorNombre(String nombre) {
        return roleRepository.findByName(nombre);
    }

    @Override
    public List<Role> obtenerTodosLosRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role guardar(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role merge(Role role) {
        return roleRepository.save(role);
    }
}
