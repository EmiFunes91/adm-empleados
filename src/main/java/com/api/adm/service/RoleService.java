package com.api.adm.service;

import com.api.adm.entity.Role;
import java.util.List;
import java.util.Optional;

public interface RoleService {
    Optional<Role> buscarPorNombre(String nombre);  // Buscar un rol por nombre
    List<Role> obtenerTodosLosRoles();              // Obtener todos los roles
    Role guardar(Role role);                        // Guardar un rol
    Role merge(Role role);
    void inicializarRoles();// Actualizar un rol (merge)
}













