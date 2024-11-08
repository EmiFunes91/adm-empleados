package com.api.adm.repository;

import com.api.adm.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByActivoTrueAndNombreContainingIgnoreCaseOrActivoTrueAndCategoriaContainingIgnoreCase(String nombre, String categoria);
    List<Producto> findByActivoTrue();
}






