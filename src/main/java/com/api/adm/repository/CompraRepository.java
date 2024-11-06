package com.api.adm.repository;

import com.api.adm.entity.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {

    @Query("SELECT c FROM Compra c LEFT JOIN FETCH c.detalles WHERE c.id = :id")
    Optional<Compra> findByIdWithDetalles(Long id);

    @Query("SELECT c FROM Compra c LEFT JOIN FETCH c.detalles")
    List<Compra> findAllWithDetalles();

    // Nuevo método para buscar por cliente o fecha
    @Query("SELECT c FROM Compra c LEFT JOIN FETCH c.detalles d WHERE " +
            "(LOWER(c.cliente.nombre) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(c.cliente.apellido) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR CAST(c.fecha AS string) LIKE CONCAT('%', :query, '%'))")
    List<Compra> findByClienteOrFechaContainingIgnoreCase(String query);
}


