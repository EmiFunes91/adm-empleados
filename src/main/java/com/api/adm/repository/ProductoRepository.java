package com.api.adm.repository;

import com.api.adm.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByNombreContainingIgnoreCaseOrCategoriaContainingIgnoreCase(String nombre, String categoria);
}


