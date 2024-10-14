package com.api.adm.repository;

import com.api.adm.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByEmail(String email);

    // Nuevo query para obtener usuarios por rol
    @Query("SELECT u FROM Usuario u JOIN u.roles r WHERE r.name = :rolName")
    List<Usuario> findUsuariosByRolName(String rolName);
}



