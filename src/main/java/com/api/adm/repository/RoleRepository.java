package com.api.adm.repository;

import com.api.adm.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);  // Método para buscar un rol por su nombre
}


