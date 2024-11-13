package com.api.adm.repository;

import com.api.adm.entity.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {

    List<Compra> findByFechaCompraBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<Compra> findByFechaCompraBetweenAndClienteId(LocalDateTime fechaInicio, LocalDateTime fechaFin, Long clienteId);
    List<Compra> findByFechaCompraBetweenAndEstado(LocalDateTime fechaInicio, LocalDateTime fechaFin, String estado);
    List<Compra> findByFechaCompraBetweenAndClienteIdAndEstado(LocalDateTime fechaInicio, LocalDateTime fechaFin, Long clienteId, String estado);
    List<Compra> findByClienteId(Long clienteId);
    List<Compra> findByEstado(String estado);

    @Query("SELECT c FROM Compra c LEFT JOIN FETCH c.compraDetalles WHERE c.fechaCompra BETWEEN :fechaInicio AND :fechaFin")
    List<Compra> findByFechaCompraBetweenWithDetalles(@Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT c FROM Compra c LEFT JOIN FETCH c.compraDetalles WHERE c.id = :id")
    Optional<Compra> findByIdWithDetalles(@Param("id") Long id);
}








