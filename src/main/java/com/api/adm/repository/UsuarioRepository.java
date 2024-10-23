package com.api.adm.repository;

import com.api.adm.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByEmail(String email);

    @Query("SELECT u FROM Usuario u JOIN u.roles r WHERE r.name = :roleName")
    List<Usuario> findUsuariosByRolName(@Param("roleName") String roleName);
}





